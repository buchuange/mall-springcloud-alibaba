package com.dawn.mall.domain;

import com.dawn.mall.utils.serializer.Date2LongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
public class Shipping {

    private Integer id;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 收货姓名
     */
    private String receiverName;

    /**
     * 收货固定电话
     */
    private String receiverPhone;

    /**
     * 收货移动电话
     */
    private String receiverMobile;

    /**
     * 省份
     */
    private String receiverProvince;

    /**
     * 城市
     */
    private String receiverCity;

    /**
     * 区/县
     */
    private String receiverDistrict;

    /**
     * 详细地址
     */
    private String receiverAddress;

    /**
     * 邮编
     */
    private String receiverZip;

    /**
     * 创建时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;
}