package com.dawn.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/5/22 22:12
 */
@Getter
@AllArgsConstructor
public enum  PaymentTypeEnum {

    ONLINE_PAYMENT(1, "在线支付");

    private final int code;

    private final String msg;
}
