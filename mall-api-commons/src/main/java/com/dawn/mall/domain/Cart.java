package com.dawn.mall.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Dawn
 * @Date: 2022/5/18 01:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    private Integer productId;

    private String productName;

    private Integer quantity;

    private Boolean productSelected;
}
