package com.dawn.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 18:28
 */
@Getter
@AllArgsConstructor
public enum ProductStatusEnum {

    ON_SALE(1, "在售"),

    OFF_SAGE(2, "下架"),

    DELETE(3, "删除");

    private final int code;

    private final String msg;
}
