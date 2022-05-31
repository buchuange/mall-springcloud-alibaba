package com.dawn.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.consts.RedisConsts;
import com.dawn.mall.domain.*;
import com.dawn.mall.enums.OrderStatusEnum;
import com.dawn.mall.enums.PaymentTypeEnum;
import com.dawn.mall.enums.ProductStatusEnum;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.exception.MallException;
import com.dawn.mall.mapper.OrderItemMapper;
import com.dawn.mall.mapper.OrderMapper;
import com.dawn.mall.mapper.OrderShippingMapper;
import com.dawn.mall.service.CartService;
import com.dawn.mall.service.OrderService;
import com.dawn.mall.service.ProductService;
import com.dawn.mall.service.ShippingService;
import com.dawn.mall.utils.KeyUtil;
import com.dawn.mall.utils.RedisUtil;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.CartVo;
import com.dawn.mall.vo.OrderItemVo;
import com.dawn.mall.vo.OrderVo;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Dawn
 * @Date: 2022/5/21 02:55
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private OrderShippingMapper orderShippingMapper;

    @Resource
    private CartService cartService;

    @Resource
    private ProductService productService;

    @Resource
    private ShippingService shippingService;


    @Override
    @Transactional
    public ResultVo<OrderVo> saveOrder(Integer userId, Integer shippingId) {

        // 收获地址校验
        Shipping shipping = getShipping(userId, shippingId);

        // 购物车校验（是否有商品，库存）
        List<Cart> cartList = getCartList(userId);

        Map<Integer, Product> map = getProductMap(cartList);

        // 订单编号
        String orderNo = KeyUtil.generateUniqueKey();

        List<OrderItem> orderItemList = new ArrayList<>();

        for (Cart cart : cartList) {

            // 查询商品
            Product product = map.get(cart.getProductId());

            if (ObjectUtils.isEmpty(product) || ProductStatusEnum.OFF_SAGE.getCode() == product.getStatus() ||
                    ProductStatusEnum.DELETE.getCode() == product.getStatus()) {

                log.info("【生成订单失败】购物车中的商品查询不到：商品Id：" + cart.getProductId());
                throw new MallException(ResultEnum.PRODUCT_NOT_EXIST.getCode(),
                        ResultEnum.PRODUCT_NOT_EXIST.getDesc() + "商品名称：" + cart.getProductName());

            }

            // 判断库存是否充足
            if (product.getStock() < cart.getQuantity()) {

                throw new MallException(ResultEnum.PRODUCT_STOCK_ERROR.getCode(),
                        ResultEnum.PRODUCT_STOCK_ERROR.getDesc() + JSONUtil.toJsonPrettyStr(cart));
            }

            // 创建订单详情
            OrderItem orderItem =  buildOrderItem(orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            // 更新商品库存
            Integer residueStock = product.getStock() - cart.getQuantity();
            product.setStock(residueStock);

            updateStock(product);
        }

        // 生成订单 订单详情 订单地址详情
        Order order = buildOrder(userId, orderNo, orderItemList);

        int resultCountForOrderShipping = orderMapper.insertSelective(order);

        if (resultCountForOrderShipping != 1) {
            throw new MallException(ResultEnum.FAILURE);
        }

        int resultCountForOrderItem = orderItemMapper.insertBatch(orderItemList);

        if (resultCountForOrderItem != orderItemList.size()) {
            throw new MallException(ResultEnum.FAILURE);
        }

        OrderShipping orderShipping = buildOrderShipping(orderNo, shipping);

        int resultCountForOrder = orderShippingMapper.insertSelective(orderShipping);

        if (resultCountForOrder != 1) {
            throw new MallException(ResultEnum.FAILURE);
        }

        // 更新购物车
        for (Cart cart : cartList) {
            deleteCart(userId, cart.getProductId());
        }

        OrderVo orderVo = buildOrderVo(order, orderItemList, orderShipping);


        return ResultVoUtil.success(orderVo);
    }

    @Override
    @Transactional
    public ResultVo<PageInfo> listOrder(Integer userId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Order> orderList = orderMapper.listAllByUserId(userId);

        Set<String> orderNoSet = orderList.stream()
                .map(Order::getOrderNo).collect(Collectors.toSet());

        List<OrderItem> orderItemList = orderItemMapper.listAllByOrderNo(orderNoSet);

        Map<String, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        List<OrderShipping> orderShippingList = orderShippingMapper.listAllByOrderNo(orderNoSet);

        Map<String, OrderShipping> shippingMap = orderShippingList.stream()
                .collect(Collectors.toMap(OrderShipping::getOrderNo, orderShipping -> orderShipping));


        List<OrderVo> orderVoList = new ArrayList<>();

        for (Order order : orderList) {

            OrderVo orderVO = buildOrderVo(order, orderItemMap.get(order.getOrderNo()), shippingMap.get(order.getOrderNo()));

            orderVoList.add(orderVO);
        }


        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVoList);

        return ResultVoUtil.success(pageInfo);
    }

    @Override
    @Transactional
    public ResultVo<OrderVo> getOrder(Integer userId, String orderNo) {

        Order order = orderMapper.getByOrderNo(orderNo);

        if (ObjectUtils.isEmpty(order) || !userId.equals(order.getUserId())) {

            log.error("【查询订单失败】用户Id：{} 订单编号：{}", userId, orderNo);
            throw new MallException(ResultEnum.ORDER_NOT_EXIST);
        }

        Set<String> orderNoSet = new HashSet<>();
        orderNoSet.add(orderNo);

        List<OrderItem> orderItemList = orderItemMapper.listAllByOrderNo(orderNoSet);

        if (CollectionUtils.isEmpty(orderItemList)) {

            log.error("【查询订单失败】订单详情为空，用户Id：{} 订单编号：{}", userId, orderNo);
            throw new MallException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        List<OrderShipping> orderShippingList = orderShippingMapper.listAllByOrderNo(orderNoSet);

        OrderVo orderVO = buildOrderVo(order, orderItemList, orderShippingList.get(0));

        return ResultVoUtil.success(orderVO);
    }

    @Override
    @Transactional
    public ResultVo<String> cancelOrder(Integer userId, String orderNo) {

        Order order = orderMapper.getByOrderNo(orderNo);

        if (ObjectUtils.isEmpty(order) || !userId.equals(order.getUserId())) {

            log.error("【取消订单失败】用户Id：{} 订单编号：{}", userId, orderNo);
            throw new MallException(ResultEnum.ORDER_NOT_EXIST);
        }

        // 只有未付款订单可以取消
        if (!(OrderStatusEnum.NO_PAY.getCode() == order.getStatus())) {

            throw new MallException(ResultEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());

        int resultCount = orderMapper.updateStatus(order);

        if (resultCount != 1) {
            throw new MallException(ResultEnum.FAILURE);
        }

        // 更新库存
        Set<String> orderNoSet = new HashSet<>();
        orderNoSet.add(orderNo);

        List<OrderItem> orderItemList = orderItemMapper.listAllByOrderNo(orderNoSet);

        for (OrderItem orderItem : orderItemList) {

            Product product = getProduct(orderItem.getProductId());
            Integer residueStock = product.getStock() + orderItem.getQuantity();
            product.setStock(residueStock);

            updateStock(product);
        }

        return ResultVoUtil.success("取消订单成功");
    }

    @Transactional
    @Override
    public void paid(String orderNo) {

        Order order = orderMapper.getByOrderNo(orderNo);

        if (ObjectUtils.isEmpty(order)) {
            throw new MallException(ResultEnum.ORDER_NOT_EXIST.getCode(),
                    ResultEnum.ORDER_NOT_EXIST.getDesc() + "订单Id：" + orderNo);
        }

        // 只有未付款订单可以变成已付款订单
        if (!(OrderStatusEnum.NO_PAY.getCode() == order.getStatus())) {

            throw new MallException(ResultEnum.ORDER_STATUS_ERROR.getCode(),
                    ResultEnum.ORDER_STATUS_ERROR.getDesc() + "订单Id：" + orderNo);
        }

        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());

        int resultCount = orderMapper.updateStatus(order);

        if (resultCount != 1) {
            throw new RuntimeException("将订单更新为已支付状态失败，订单Id: " + orderNo);
        }

    }

    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, OrderShipping orderShipping) {

        OrderVo orderVo = order.convertToVo();

        List<OrderItemVo> orderItemVOList = orderItemList.stream()
                .map(OrderItem::convertToVo).collect(Collectors.toList());

        orderVo.setOrderItemVoList(orderItemVOList);
        orderVo.setShippingId(orderShipping.getShippingId());
        orderVo.setShippingVo(orderShipping);

        return orderVo;
    }

    private OrderShipping buildOrderShipping(String orderNo, Shipping shipping) {

        OrderShipping orderShipping = new OrderShipping();
        BeanUtils.copyProperties(shipping, orderShipping);
        orderShipping.setShippingId(shipping.getId());
        orderShipping.setOrderNo(orderNo);

        return orderShipping;
    }

    private Order buildOrder(Integer userId, String orderNo, List<OrderItem> orderItemList) {

        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(orderNo);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.ONLINE_PAYMENT.getCode());
        order.setPostage(10);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }

    private OrderItem buildOrderItem(String orderNo, Integer quantity, Product product) {

        OrderItem orderItem = new OrderItem();

        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(BigDecimal.valueOf(quantity).multiply(product.getPrice()));

        return orderItem;
    }

    /**
     * 调用用户微服务获取收货地址
     * @param userId
     * @param shippingId
     * @return
     */
    private Shipping getShipping(Integer userId, Integer shippingId) {

        ResultVo<Shipping> shippingResultVo = shippingService.getByIdAndUid(shippingId, userId);

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == shippingResultVo.getStatus()) {

            log.error("【获取地址失败】调用用户微服务时触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        Shipping shipping = shippingResultVo.getData();

        if (ObjectUtils.isEmpty(shipping)) {
            throw new MallException(ResultEnum.SHIPPING_NOT_EXIST);
        }

        return shipping;
    }

    /**
     * 调用购物车微服务获取购物车列表
     * @param userId
     * @return
     */
    private List<Cart> getCartList(Integer userId) {

        ResultVo<List<Cart>> cartListResultVo = cartService.listCartsByUserId(userId);

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == cartListResultVo.getStatus()) {

            log.error("【获取购物车列表失败】调用购物车微服务时触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        List<Cart> cartList = cartListResultVo.getData().stream()
                .filter(Cart::getProductSelected).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartList)) {

            throw new MallException(ResultEnum.CART_PRODUCT_NOT_SELECTED);
        }

        return cartList;
    }

    /**
     * 获取购物车中的商品列表
     * @param cartList
     * @return
     */
    private Map<Integer, Product> getProductMap(List<Cart> cartList) {

        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId).collect(Collectors.toSet());

        ResultVo<List<Product>> resultVo = productService.listAllByIdSet(productIdSet);

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == resultVo.getStatus()) {

            log.error("【获取商品列表失败】调用商品微服务时触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        List<Product> productList = resultVo.getData();

        return productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    /**
     * 获取商品信息
     * @param
     * @return
     */
    private Product getProduct(Integer productId) {

        ResultVo<Product> productResultVo = productService.getProductById(productId);

        if (ResultEnum.PRODUCT_NOT_EXIST.getCode() == productResultVo.getStatus()) {

            log.info("【生成订单失败】购物车中的商品查询不到：商品Id：" + productId);
            throw new MallException(ResultEnum.PRODUCT_NOT_EXIST.getCode(),
                    ResultEnum.PRODUCT_NOT_EXIST.getDesc() + "商品ID：" + productId);
        }

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == productResultVo.getStatus()) {

            log.error("【获取商品信息失败】调用商品微服务时触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        return productResultVo.getData();
    }

    /**
     * 更新商品库存
     * @param product
     */
    private void updateStock(Product product) {

        ResultVo<Integer> resultVo = productService.updateStock(product);

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == resultVo.getStatus()) {

            log.error("【更新商品库存失败】调用商品微服务时触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        if (ObjectUtils.isEmpty(resultVo.getData())) {

            log.error("【更新商品库存失败】");
            throw new MallException(ResultEnum.FAILURE);
        }
    }

    /**
     * 更新购物车
     */
    private void deleteCart(Integer userId, Integer productId) {

        ResultVo<CartVo> cartVoResultVo = cartService.deleteCart(userId, productId);

        if (ResultEnum.MICROSERVICE_INVOKE_ERROR.getCode() == cartVoResultVo.getStatus()) {

            log.error("【更新购物车失败】调用购物车微服务时触发了熔断机制");
            throw new MallException(ResultEnum.FAILURE);
        }

        if (ObjectUtils.isEmpty(cartVoResultVo.getData())) {

            log.error("【更新购物车失败】");
            throw new MallException(ResultEnum.FAILURE);
        }
    }
}
