package com.dawn.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 02:34
 */
@Data
public class CategoryVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategories;
}
