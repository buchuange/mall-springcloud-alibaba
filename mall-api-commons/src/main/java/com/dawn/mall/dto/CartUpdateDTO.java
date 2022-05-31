package com.dawn.mall.dto;

import lombok.Data;

/**
 * @Author: Dawn
 * @Date: 2022/5/20 20:53
 */
@Data
public class CartUpdateDTO {

    private Integer quantity;

    private Boolean selected;
}
