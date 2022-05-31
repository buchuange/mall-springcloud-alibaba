package com.dawn.mall.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: Dawn
 * @Date: 2022/5/22 20:41
 */
@Data
public class OrderCreateDTO {

    @NotNull
    private Integer shippingId;
}
