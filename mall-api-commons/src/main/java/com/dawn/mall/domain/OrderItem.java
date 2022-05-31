package com.dawn.mall.domain;

import com.dawn.mall.dto.Converter;
import com.dawn.mall.vo.OrderItemVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItem {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 商品Id
     */
    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片地址
     */
    private String productImage;

    /**
     * 生成订单时的商品单价，单位是元,保留两位小数
     */
    private BigDecimal currentUnitPrice;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品总价
     */
    private BigDecimal totalPrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public OrderItemVo convertToVo() {

        OrderItemVoConvert orderItemVoConvert = new OrderItemVoConvert();

        return orderItemVoConvert.convert(this);
    }


    private static class OrderItemVoConvert implements Converter<OrderItem, OrderItemVo> {

        @Override
        public OrderItemVo convert(OrderItem orderItem) {

            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(orderItem, orderItemVo);

            return orderItemVo;
        }
    }
}