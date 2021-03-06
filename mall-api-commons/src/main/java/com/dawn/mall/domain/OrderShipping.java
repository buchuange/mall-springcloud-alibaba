package com.dawn.mall.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OrderShipping {

    private Integer id;

    private String orderNo;

    private Integer shippingId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Date createTime;

    private Date updateTime;
}