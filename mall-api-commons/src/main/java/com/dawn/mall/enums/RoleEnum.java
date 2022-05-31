package com.dawn.mall.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ADMIN(0),

    CUSTOMER(1);

    private final int code;

    RoleEnum(int code) {
        this.code = code;
    }
}
