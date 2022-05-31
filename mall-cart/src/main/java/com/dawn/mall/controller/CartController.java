package com.dawn.mall.controller;

import com.dawn.mall.domain.Cart;
import com.dawn.mall.domain.User;
import com.dawn.mall.dto.CartAddDTO;
import com.dawn.mall.dto.CartUpdateDTO;
import com.dawn.mall.service.CartService;
import com.dawn.mall.utils.MallUtil;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.CartProductVo;
import com.dawn.mall.vo.CartVo;
import com.dawn.mall.vo.ResultVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/5/18 02:28
 */
@RestController
public class CartController {

    @Resource
    private CartService cartService;

    @PostMapping("/carts")
    public ResultVo<CartVo> saveCart(@Valid @RequestBody CartAddDTO cartAddDTO,
                                     HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);


        return cartService.saveCart(user.getId(), cartAddDTO);
    }

    @GetMapping("/carts")
    public ResultVo<CartVo> listCarts(HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return cartService.listCarts(user.getId());
    }

    @PutMapping("/carts/{productId}")
    public ResultVo<CartVo> updateCart(@PathVariable("productId") Integer productId,
                                       @Valid @RequestBody CartUpdateDTO cartUpdateDTO,
                                       HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return cartService.updateCart(user.getId(), productId, cartUpdateDTO);
    }

    @DeleteMapping("/carts/{productId}")
    public ResultVo<CartVo> deleteCart(@PathVariable("productId") Integer productId,
                                       HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return cartService.deleteCart(user.getId(), productId);
    }

    @PutMapping("/carts/selectAll")
    public ResultVo<CartVo> selectAll(HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return cartService.selectAll(user.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResultVo<CartVo> unSelectAll(HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return cartService.unSelectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResultVo<Integer> countCart(HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return cartService.countCart(user.getId());
    }

    @GetMapping("/carts/order")
    public ResultVo<List<Cart>> listCartsByUserId(@RequestParam("userId") Integer userId) {

        List<Cart> cartList = cartService.listCartsByUserId(userId);

        return ResultVoUtil.success(cartList);
    }

    @DeleteMapping("/carts/order/delete")
    public ResultVo<CartVo> deleteCart(@RequestParam("userId") Integer userId,
                                       @RequestParam("productId") Integer productId) {


        return cartService.deleteCart(userId, productId);
    }
}
