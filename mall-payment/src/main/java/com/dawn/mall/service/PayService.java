package com.dawn.mall.service;

import com.dawn.mall.domain.PayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * @Author: Dawn
 * @Date: 2022/5/23 03:50
 */
public interface PayService {

    PayResponse create(Integer userId, String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    void asyncNotify(String notifyData);

    PayInfo getByOrderNo(String orderNo);
}
