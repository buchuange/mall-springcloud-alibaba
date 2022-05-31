package com.dawn.mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Dawn
 * @Date: 2022/5/23 03:23
 */
@Component
@Data
@ConfigurationProperties(prefix = "wechat")
public class WeChatAccountConfig {

    /**
     * 公众号Appid
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String mchKey;

    /**
     * 支付完成后的异步通知地址.
     */
    private String notifyUrl;

    /**
     * 支付完成后的同步返回地址.
     */
    private String returnUrl;
}
