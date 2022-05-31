package com.dawn.mall.configuration;

import com.dawn.mall.consts.MallConsts;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Dawn
 * @Date: 2022/5/18 01:22
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        requestTemplate.header(MallConsts.AUTHORIZE_KEY, MallConsts.AUTHORIZE_VALUE);
    }
}
