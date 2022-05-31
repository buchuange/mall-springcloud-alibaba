package com.dawn.mall.service;

import com.dawn.mall.configuration.FeignConfiguration;
import com.dawn.mall.domain.Cart;
import com.dawn.mall.service.fallback.CartServiceFallBack;
import com.dawn.mall.vo.CartVo;
import com.dawn.mall.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "server-cart", configuration = FeignConfiguration.class,
        fallback = CartServiceFallBack.class)
public interface CartService {

    @GetMapping("/carts/order")
    ResultVo<List<Cart>> listCartsByUserId(@RequestParam("userId") Integer userId);

    @DeleteMapping("/carts/order/delete")
    ResultVo<CartVo> deleteCart(@RequestParam("userId") Integer userId,
                                       @RequestParam("productId") Integer productId);
}
