package com.dawn.mall.mapper;

import com.dawn.mall.domain.Category;
import com.dawn.mall.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {

    List<Category> listUpAll(@Param("status") Integer status);
}
