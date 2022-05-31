package com.dawn.mall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Dawn
 * @Date: 2022/5/11 18:05
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    MICROSERVICE_INVOKE_ERROR(-2, "微服务调用异常"),

    ERROR(-1, "服务端异常"),

    FAILURE(500, "执行失败"),

    SUCCESS(0, "执行成功"),

    PARAM_ERROR(400, "传入参数错误"),

    AUTHORIZE_ERROR(420, "请通过网关访问资源"),

    /**
     * 用户错误
     */
    USER_NOT_LOGIN(2001, "用户未登录, 请先登录！"),

    USERNAME_OR_PASSWORD_ERROR(2002, "用户名或密码错误"),

    USERNAME_EXIST(2003, "该用户名太受欢迎了！"),

    EMAIL_EXIST(2004, "邮箱已存在"),

    SHIPPING_NOT_EXIST(2005, "收获地址不存在"),

    /**
     * 商品错误
     */
    PRODUCT_NOT_EXIST(3001, "商品不存在，该商品已下架或删除！"),

    PRODUCT_STOCK_ERROR(3002, "商品库存不足"),

    /**
     * 购物车错误
     */
    CART_PRODUCT_NOT_EXIST(4001, "购物车里无此商品"),

    CART_PRODUCT_NOT_SELECTED(4002, "您还没有选择宝贝哦！"),

    /**
     * 订单错误
     */
    SEC_KILL_FAIL(5001, "秒杀商品失败，当前人数过多，请稍后再试！"),

    ORDER_NOT_EXIST(5002, "该用户没有此订单"),

    ORDER_DETAIL_NOT_EXIST(5003, "订单详情不存在"),

    ORDER_STATUS_ERROR(5004, "订单状态不正确"),

    /**
     * 支付错误
     */
    PAY_PLATFORM_ERROR(6001, "暂不支持该平台的支付！"),

    PAY_INFO_NOT_EXIST(6002, "订单支付信息不存在"),

    WECHAT_NOTIFY_MONEY_VERIFY_ERROR(6003, "微信支付异步通知金额交易不通过");

    private final int code;

    private final String desc;
}