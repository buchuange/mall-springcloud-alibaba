package com.dawn.mall.vo;

import com.dawn.mall.domain.OrderShipping;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: Dawn
 * @Date: 2022/5/21 02:34
 */
@Data
public class OrderVo {

    private String orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private List<OrderItemVo> orderItemVoList;

    private Integer shippingId;

    private OrderShipping shippingVo;

}
