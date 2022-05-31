package com.dawn.mall.service;

import com.dawn.mall.domain.Product;
import com.dawn.mall.vo.ProductVo;
import com.dawn.mall.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ProductService {

    ResultVo<PageInfo> listOnSaleProducts(Integer categoryId, Integer pageNum, Integer pageSize);

    ResultVo<Product> getProductById(Integer productId);

    ResultVo<List<Product>> listAllByIdSet(Set<Integer> productIdSet);

    ResultVo<Integer> updateStock(Product product);
}
