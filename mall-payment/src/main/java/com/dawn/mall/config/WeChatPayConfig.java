package com.dawn.mall.config;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: Dawn
 * @Date: 2022/5/23 03:28
 */
@Configuration
public class WeChatPayConfig {

    @Resource
    WeChatAccountConfig weChatAccountConfig;

    @Bean
    public BestPayService bestPayService() {

        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig());

        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {

        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(weChatAccountConfig.getAppId());
        wxPayConfig.setMchId(weChatAccountConfig.getMchId());
        wxPayConfig.setMchKey(weChatAccountConfig.getMchKey());
        wxPayConfig.setNotifyUrl(weChatAccountConfig.getNotifyUrl());
        wxPayConfig.setReturnUrl(weChatAccountConfig.getReturnUrl());

        return wxPayConfig;
    }
}
