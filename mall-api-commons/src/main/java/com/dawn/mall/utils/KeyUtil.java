package com.dawn.mall.utils;

import java.util.Random;

/**
 * @Author: Dawn
 * @Date: 2022/4/14 02:15
 */
public class KeyUtil {

    /**
     * 生成唯一主键
     * 格式：时间+随机数
     * @return
     */
    public static synchronized String generateUniqueKey() {

        Random random = new Random();

        int num = random.nextInt(900000) + 100000;

        long key = System.currentTimeMillis() + num;

        return String.valueOf(key);
    }
}
