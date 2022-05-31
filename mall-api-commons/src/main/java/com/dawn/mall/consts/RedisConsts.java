package com.dawn.mall.consts;

/**
 * @Author: Dawn
 * @Date: 2022/5/14 00:55
 */
public class RedisConsts {

    public static final String TOKEN_PREFIX = "user-token:%s";

    public static final String CART_REDIS_KEY_TEMPLATE = "user-cart:%d";

    public static final int EXPIRE = 1800;

    public static final String LOCK_KEY = "Star";

    public static final int LOCK_TIMEOUT = 10 * 1000;
}
