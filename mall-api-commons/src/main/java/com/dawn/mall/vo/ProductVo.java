package com.dawn.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 18:12
 */
@Data
public class ProductVo {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;

}
