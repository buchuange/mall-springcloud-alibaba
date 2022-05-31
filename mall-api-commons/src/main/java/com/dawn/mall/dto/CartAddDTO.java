package com.dawn.mall.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: Dawn
 * @Date: 2022/5/18 00:54
 */
@Data
public class CartAddDTO {

    @NotNull
    private Integer productId;

    private Boolean selected;
}
