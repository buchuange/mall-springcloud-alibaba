package com.dawn.mall.controller;

import com.dawn.mall.domain.Product;
import com.dawn.mall.service.ProductService;
import com.dawn.mall.vo.ProductVo;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.util.List;
import java.util.Set;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 18:57
 */
@RestController
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/products")
    public ResultVo<PageInfo> listProducts(@RequestParam(required = false) Integer categoryId,
                                           @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        return productService.listOnSaleProducts(categoryId, pageNum, pageSize);
    }

    @GetMapping("/products/{productId}")
    public ResultVo<Product> getProductById(@PathVariable("productId") Integer productId) {

        return productService.getProductById(productId);
    }

    @GetMapping("/products/ids")
    public ResultVo<List<Product>> listAllByIdSet(@RequestParam("productIdSet") Set<Integer> productIdSet) {

        return productService.listAllByIdSet(productIdSet);
    }

    @PutMapping("/products/updateStock")
    public ResultVo<Integer> updateStock(@RequestBody Product product) {

        return productService.updateStock(product);
    }
}
