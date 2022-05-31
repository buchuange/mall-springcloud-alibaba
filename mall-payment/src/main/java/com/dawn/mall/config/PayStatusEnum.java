package com.dawn.mall.config;

import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.exception.MallException;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayStatusEnum {

    WX(1, "微信支付");

    private final int code;

    private final String msg;

    public static PayStatusEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {

        for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payStatusEnum.name())) {
                return payStatusEnum;
            }
        }

        throw new MallException(ResultEnum.PAY_PLATFORM_ERROR);
    }
}
