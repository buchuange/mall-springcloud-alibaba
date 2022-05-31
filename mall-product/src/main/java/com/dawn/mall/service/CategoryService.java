package com.dawn.mall.service;

import com.dawn.mall.vo.CategoryVo;
import com.dawn.mall.vo.ResultVo;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    ResultVo<List<CategoryVo>> listUpAll();

    void findSubCategoryId(Integer categoryId, Set<Integer> categoryIdSet);
}
