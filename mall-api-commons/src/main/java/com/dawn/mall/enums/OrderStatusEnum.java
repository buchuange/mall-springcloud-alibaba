package com.dawn.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/5/22 22:10
 */
@Getter
@AllArgsConstructor
public enum  OrderStatusEnum {

    SUCCESS(200, "支付成功"),

    CANCELED(0, "已取消"),

    NO_PAY(10, "未付款"),

    PAID(20, "已付款"),

    SHIPPED(40, "已发货"),

    TRADE_SUCCESS(50, "交易成功"),

    TRADE_CLOSE(60, "交易关闭");

    private final int code;

    private final String desc;
}
