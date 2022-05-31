package com.dawn.mall.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Dawn
 * @Date: 2022/5/15 20:36
 */
@Component
@Slf4j
public class RedisUtil {

    private static StringRedisTemplate stringRedisTemplate;


    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {

        RedisUtil.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 普通缓存放入并设置时间
     */

    public static void set(String key, String value, long time) {

        stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public static void expire(String key, long time) {

        stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存
     */
    public static String get(String key) {

        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     */
    public static void delete(String key) {

        stringRedisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 设置 hash类型缓存
     */
    public static void hset(String key, Object hashKey, Object value) {

        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     *  获取 hash类型缓存
     */
    public static Object hget(String key, Object hashKey) {

        return stringRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 删除 hash类型缓存
     */
    public static void hdel(String key, Object... hashKeys) {

        stringRedisTemplate.opsForHash().delete(key, hashKeys);
    }

    public static Map<Object, Object> entries(String key) {

        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 加锁
     * setnx + getset
     * @param key
     * @param value 当前时间+超时时间 150
     * @return
     */
    public static boolean lock(String key, String value) {
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        String currentValue = stringRedisTemplate.opsForValue().get(key);
        /*
            如果锁过期
            在业务处理过程中可能由于某种未知异常，导致死锁产生，下面是让死锁解锁的代码
         */
        if (StringUtils.hasLength(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            // 获取上一个锁的时间
            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key, value);
            if (StringUtils.hasLength(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    public static void unlock(String key, String value) {
        try {
            String currentValue = stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.hasLength(currentValue) && currentValue.equals(value)) {
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁异常, {}", e);
        }
    }

}
