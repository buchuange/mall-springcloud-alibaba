package com.dawn.mall.service;

import com.dawn.mall.configuration.FeignConfiguration;
import com.dawn.mall.domain.Product;
import com.dawn.mall.service.fallback.ProductServiceFallBack;
import com.dawn.mall.vo.ProductVo;
import com.dawn.mall.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(value = "server-product", configuration = FeignConfiguration.class,
        fallback = ProductServiceFallBack.class)
public interface ProductService {

    @GetMapping("/products/{productId}")
    ResultVo<Product> getProductById(@PathVariable("productId") Integer productId);

    @GetMapping("/products/ids")
    ResultVo<List<Product>> listAllByIdSet(@RequestParam("productIdSet") Set<Integer> productIdSet);

}
