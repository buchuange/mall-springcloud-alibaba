package com.dawn.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/5/17 03:04
 */
@Getter
@AllArgsConstructor
public enum CategoryStatusEnum {

    NORMAL(1, "正常"),

    OBSOLETE(2, "已废弃");

    private final int code;

    private final String msg;
}
