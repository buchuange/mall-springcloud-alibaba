package com.dawn.mall.consts;

/**
 * @Author: Dawn
 * @Date: 2022/5/15 23:14
 */
public class MallConsts {

    /**
     * 是否通过网关访问资源的验证Key
     */
    public static final String AUTHORIZE_KEY = "PAUL_GEORGE";

    /**
     * 是否通过网关访问资源的验证Value
     */
    public static final String AUTHORIZE_VALUE = "MVP";

    /**
     * 类目的根Id为0
     */
    public static final int CATEGORY_ROOT_PARENT_ID = 0;

    /**
     * 加入购物车的数量 添加商品永远是以1累加
     */
    public static final int CART_ADD_QUANTITY = 1;

    /**
     * 支付完成后将信息写入消息队列
     */
    public static final String QUEUE_PAY_NOTIFY = "payNotify";
}
