package com.dawn.mall.mapper;

import com.dawn.mall.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ProductMapper {

    List<Product> listUpAllByCategoryIdSet(@Param("status") Integer status,
                                           @Param("categoryIdSet") Set<Integer> categoryIdSet);

    Product findById(@Param("productId") Integer productId);

    List<Product> listAllByIdSet(@Param("productIdSet") Set<Integer> productIdSet);

    int updateStock(Product product);
}
