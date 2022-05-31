package com.dawn.mall.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayInfo {

    private Integer id;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付平台:1-微信,2-支付宝
     */
    private Integer payPlatform;

    /**
     * 支付流水号
     */
    private String platformNumber;

    /**
     * 支付状态
     */
    private String platformStatus;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}