package com.dawn.mall.domain;

import com.dawn.mall.dto.Converter;
import com.dawn.mall.vo.OrderVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 实际付款金额,单位是元,保留两位小数
     */
    private BigDecimal payment;

    /**
     * 支付类型,1-在线支付
     */
    private Integer paymentType;

    /**
     * 运费,单位是元
     */
    private Integer postage;

    /**
     * 订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 发货时间
     */
    private Date sendTime;

    /**
     * 交易完成时间
     */
    private Date endTime;

    /**
     * 交易关闭时间
     */
    private Date closeTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public OrderVo convertToVo() {

        OrderVoConvert orderVoConvert = new OrderVoConvert();

        return orderVoConvert.convert(this);
    }


    private static class OrderVoConvert implements Converter<Order, OrderVo> {

        @Override
        public OrderVo convert(Order order) {

            OrderVo orderVo = new OrderVo();
            BeanUtils.copyProperties(order, orderVo);

            return orderVo;
        }
    }
}