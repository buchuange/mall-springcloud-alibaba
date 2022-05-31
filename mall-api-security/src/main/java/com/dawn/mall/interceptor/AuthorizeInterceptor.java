package com.dawn.mall.interceptor;

import com.dawn.mall.consts.MallConsts;
import com.dawn.mall.exception.AuthorizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Dawn
 * @Date: 2022/5/15 23:19
 */
@Slf4j
public class AuthorizeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String secretKey = request.getHeader(MallConsts.AUTHORIZE_KEY);

        if(!StringUtils.hasLength(secretKey) || !secretKey.equals(MallConsts.AUTHORIZE_VALUE)) {

            log.warn("【用户试图越过网关直接访问】");
            throw new AuthorizeException();
        }

        return true;
    }
}
