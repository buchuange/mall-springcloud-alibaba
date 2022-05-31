package com.dawn.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.consts.MallConsts;
import com.dawn.mall.consts.RedisConsts;
import com.dawn.mall.domain.Cart;
import com.dawn.mall.domain.Product;
import com.dawn.mall.dto.CartAddDTO;
import com.dawn.mall.dto.CartUpdateDTO;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.exception.MallException;
import com.dawn.mall.service.CartService;
import com.dawn.mall.service.ProductService;
import com.dawn.mall.utils.RedisUtil;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.CartProductVo;
import com.dawn.mall.vo.CartVo;
import com.dawn.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Dawn
 * @Date: 2022/5/18 02:07
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Resource
    private ProductService productService;

    /**
     * 加入购物车
     *
     * @param userId
     * @param cartAddDTO
     * @return
     */
    @Override
    public ResultVo<CartVo> saveCart(Integer userId, CartAddDTO cartAddDTO) {

        ResultVo<Product> productResultVo = productService.getProductById(cartAddDTO.getProductId());

        if (ResultEnum.PRODUCT_NOT_EXIST.getCode() == productResultVo.getStatus()) {

            log.info("【添加购物车失败】商品查询不到：商品Id：" + cartAddDTO.getProductId());
            throw new MallException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == productResultVo.getStatus()) {

            log.error("【商品微服务调用异常】触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        Product product = productResultVo.getData();

        if (product.getStock() <= 0) {

            throw new MallException(ResultEnum.PRODUCT_STOCK_ERROR);
        }

        long time = System.currentTimeMillis() + RedisConsts.LOCK_TIMEOUT;

        //加锁
        if (!RedisUtil.lock(String.valueOf(product.getId()), String.valueOf(time))) {
            throw new MallException(ResultEnum.SEC_KILL_FAIL);
        }


        // 先从Redis中获取购物车
        String cartKey = String.format(RedisConsts.CART_REDIS_KEY_TEMPLATE, userId);

        String cartValue = (String) RedisUtil.hget(cartKey, String.valueOf(product.getId()));

        Cart cart;

        if (!StringUtils.hasLength(cartValue)) {
            // 没有该商品 新增
            cart = new Cart(product.getId(), product.getName(), MallConsts.CART_ADD_QUANTITY, cartAddDTO.getSelected());
        } else {
            // 有了 数量+1
            cart = JSONUtil.toBean(cartValue, Cart.class);
            cart.setQuantity(cart.getQuantity() + MallConsts.CART_ADD_QUANTITY);
        }

        RedisUtil.hset(cartKey, String.valueOf(product.getId()), JSONUtil.toJsonStr(cart));


        //解锁
        RedisUtil.unlock(String.valueOf(product.getId()), String.valueOf(time));

        return listCarts(userId);
    }

    /**
     * 获取用户的购物车列表
     *
     * @param userId
     * @return
     */
    @Override
    public ResultVo<CartVo> listCarts(Integer userId) {

        List<Cart> cartList = listCartsByUserId(userId);

        if (CollectionUtils.isEmpty(cartList)) {

            return ResultVoUtil.success(new CartVo());
        }

        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId).collect(Collectors.toSet());

        ResultVo<List<Product>> resultVo = productService.listAllByIdSet(productIdSet);

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == resultVo.getStatus()) {

            log.error("【商品微服务调用异常】触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        List<Product> productList = resultVo.getData();

        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;


        List<CartProductVo> cartProductVoList = new ArrayList<>();

        for (Cart cart : cartList) {

            Product product = map.get(cart.getProductId());

            if (!ObjectUtils.isEmpty(product)) {

                CartProductVo cartProductVo = buildCartProductVo(cart, product);

                cartProductVoList.add(cartProductVo);

                if (!cart.getProductSelected()) {
                    selectAll = false;
                }

                if (cart.getProductSelected()) {
                    // 计算总价（只计算选中的）
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                    cartTotalQuantity += cart.getQuantity();
                }
            }
        }

        CartVo cartVo = new CartVo();
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);

        return ResultVoUtil.success(cartVo);
    }

    @Override
    public ResultVo<CartVo> updateCart(Integer userId, Integer productId, CartUpdateDTO cartUpdateDTO) {

        String cartKey = String.format(RedisConsts.CART_REDIS_KEY_TEMPLATE, userId);

        String cartValue = (String) RedisUtil.hget(cartKey, String.valueOf(productId));

        if (!StringUtils.hasLength(cartValue)) {

            log.info("【更新购物车时无此商品】用户Id:{}, 商品Id:{}", userId, productId);
            throw new MallException(ResultEnum.CART_PRODUCT_NOT_EXIST);
        }

        Cart cart = JSONUtil.toBean(cartValue, Cart.class);

        if (!ObjectUtils.isEmpty(cartUpdateDTO.getQuantity()) && cartUpdateDTO.getQuantity() > 1) {
            cart.setQuantity(cartUpdateDTO.getQuantity());
        }
        if (!ObjectUtils.isEmpty(cartUpdateDTO.getSelected())) {
            cart.setProductSelected(cartUpdateDTO.getSelected());
        }

        RedisUtil.hset(cartKey, String.valueOf(productId), JSONUtil.toJsonStr(cart));

        return listCarts(userId);
    }

    @Override
    public ResultVo<CartVo> deleteCart(Integer userId, Integer productId) {

        String cartKey = String.format(RedisConsts.CART_REDIS_KEY_TEMPLATE, userId);

        String cartValue = (String) RedisUtil.hget(cartKey, String.valueOf(productId));

        if (!StringUtils.hasLength(cartValue)) {

            log.info("【删除购物车时无此商品】用户Id:{}, 商品Id:{}", userId, productId);
            throw new MallException(ResultEnum.CART_PRODUCT_NOT_EXIST);
        }

        RedisUtil.hdel(cartKey, String.valueOf(productId));

        return listCarts(userId);
    }

    @Override
    public ResultVo<CartVo> selectAll(Integer userId) {

        String cartKey = String.format(RedisConsts.CART_REDIS_KEY_TEMPLATE, userId);

        List<Cart> cartList = listCartsByUserId(userId);

        for (Cart cart : cartList) {
            cart.setProductSelected(true);
            RedisUtil.hset(cartKey, String.valueOf(cart.getProductId()), JSONUtil.toJsonStr(cart));
        }

        return listCarts(userId);
    }

    @Override
    public ResultVo<CartVo> unSelectAll(Integer userId) {

        String cartKey = String.format(RedisConsts.CART_REDIS_KEY_TEMPLATE, userId);

        List<Cart> cartList = listCartsByUserId(userId);

        for (Cart cart : cartList) {
            cart.setProductSelected(false);
            RedisUtil.hset(cartKey, String.valueOf(cart.getProductId()), JSONUtil.toJsonStr(cart));
        }

        return listCarts(userId);
    }

    @Override
    public ResultVo<Integer> countCart(Integer userId) {

        Integer countCart = listCartsByUserId(userId).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);
        
        return ResultVoUtil.success(countCart);
    }

    private CartProductVo buildCartProductVo(Cart cart, Product product) {

        CartProductVo cartProductVO = new CartProductVo();

        cartProductVO.setProductId(product.getId());
        cartProductVO.setQuantity(cart.getQuantity());
        cartProductVO.setProductName(product.getName());
        cartProductVO.setProductSubtitle(product.getSubtitle());
        cartProductVO.setProductMainImage(product.getMainImage());
        cartProductVO.setProductPrice(product.getPrice());
        cartProductVO.setProductStatus(product.getStatus());
        cartProductVO.setProductTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        cartProductVO.setProductStock(product.getStock());
        cartProductVO.setProductSelected(cart.getProductSelected());

        return cartProductVO;
    }

    public List<Cart> listCartsByUserId(Integer userId) {

        String cartKey = String.format(RedisConsts.CART_REDIS_KEY_TEMPLATE, userId);

        Map<Object, Object> map = RedisUtil.entries(cartKey);

        List<Cart> cartList = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : map.entrySet()) {

            Cart cart = JSONUtil.toBean((String) entry.getValue(), Cart.class);

            cartList.add(cart);
        }

        return cartList;
    }
}
