package com.dawn.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/5/18 00:48
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private Boolean selectedAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
