package com.dawn.mall.configuration;

import com.dawn.mall.interceptor.AuthorizeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Dawn
 * @Date: 2022/3/6 21:38
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    // mvc:interceptors
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        String[] addPathPatterns = {"/**"};


        registry.addInterceptor(new AuthorizeInterceptor()).addPathPatterns(addPathPatterns);
    }
}
