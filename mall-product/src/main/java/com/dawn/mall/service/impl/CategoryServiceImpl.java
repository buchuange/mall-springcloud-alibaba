package com.dawn.mall.service.impl;

import com.dawn.mall.consts.MallConsts;
import com.dawn.mall.domain.Category;
import com.dawn.mall.enums.CategoryStatusEnum;
import com.dawn.mall.mapper.CategoryMapper;
import com.dawn.mall.service.CategoryService;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.CategoryVo;
import com.dawn.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 03:13
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 查询正常状态的类目列表
     * @return
     */
    @Override
    public ResultVo<List<CategoryVo>> listUpAll() {

        List<Category> categoryList = categoryMapper.listUpAll(CategoryStatusEnum.NORMAL.getCode());

        List<CategoryVo> categoryVoList = categoryList.stream()
                .filter(category -> category.getParentId().equals(MallConsts.CATEGORY_ROOT_PARENT_ID))
                .map(Category::convertToVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder))
                .collect(Collectors.toList());

        setSubCategories(categoryList, categoryVoList);

        return ResultVoUtil.success(categoryVoList);
    }


    /**
     * 设置子类目
     * @param categoryList
     * @param categoryVoList
     */
    private void setSubCategories(List<Category> categoryList, List<CategoryVo> categoryVoList) {

        for (CategoryVo categoryVo : categoryVoList) {

            List<CategoryVo> subCategories = new ArrayList<>();

            for (Category category : categoryList) {

                if (category.getParentId().equals(categoryVo.getId())) {
                    subCategories.add(category.convertToVo());

                    setSubCategories(categoryList, subCategories);

                }
            }

            subCategories.sort(Comparator.comparing(CategoryVo::getSortOrder));
            categoryVo.setSubCategories(subCategories);
        }
    }

    /**
     * 查询所有子类目id
     * @param categoryId
     * @param categoryIdSet
     */
    @Override
    public void findSubCategoryId(Integer categoryId, Set<Integer> categoryIdSet) {

        List<Category> categoryList = categoryMapper.listUpAll(CategoryStatusEnum.NORMAL.getCode());

        findSubCategoryId(categoryId, categoryIdSet, categoryList);
    }

    private void findSubCategoryId(Integer categoryId, Set<Integer> categoryIdSet, List<Category> categoryList) {

        for (Category category : categoryList) {

            if (category.getParentId().equals(categoryId)) {

                Integer id = category.getId();

                categoryIdSet.add(id);

                findSubCategoryId(id, categoryIdSet, categoryList);
            }
        }
    }
}
