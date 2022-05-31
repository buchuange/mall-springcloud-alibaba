package com.dawn.mall.service;

import com.dawn.mall.domain.Cart;
import com.dawn.mall.dto.CartAddDTO;
import com.dawn.mall.dto.CartUpdateDTO;
import com.dawn.mall.vo.CartVo;
import com.dawn.mall.vo.ResultVo;

import java.util.List;

public interface CartService {

    ResultVo<CartVo> saveCart(Integer userId, CartAddDTO cartAddDTO);

    ResultVo<CartVo> listCarts(Integer userId);

    ResultVo<CartVo> updateCart(Integer userId, Integer productId, CartUpdateDTO cartUpdateDTO);

    ResultVo<CartVo> deleteCart(Integer userId, Integer productId);

    ResultVo<CartVo> selectAll(Integer userId);

    ResultVo<CartVo> unSelectAll(Integer userId);

    ResultVo<Integer> countCart(Integer userId);

    List<Cart> listCartsByUserId(Integer userId);
}
