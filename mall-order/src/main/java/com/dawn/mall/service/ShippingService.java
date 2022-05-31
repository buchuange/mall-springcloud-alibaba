package com.dawn.mall.service;

import com.dawn.mall.configuration.FeignConfiguration;
import com.dawn.mall.domain.Shipping;
import com.dawn.mall.service.fallback.ProductServiceFallBack;
import com.dawn.mall.service.fallback.ShippingServiceFallBack;
import com.dawn.mall.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "server-user", configuration = FeignConfiguration.class,
        fallback = ShippingServiceFallBack.class)
public interface ShippingService {

    @GetMapping("/shippings/order")
    ResultVo<Shipping> getByIdAndUid(@RequestParam("shippingId") Integer shippingId,
                                            @RequestParam("userId") Integer userId);
}
