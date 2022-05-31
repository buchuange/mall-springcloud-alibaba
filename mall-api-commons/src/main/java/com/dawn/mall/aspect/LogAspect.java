package com.dawn.mall.aspect;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Dawn
 * @Date: 2022/5/11 23:58
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut(value = "execution(* com.dawn.mall.controller..*.*(..))")
    public void log() {

    }

    @Before(value = "log()")
    public void doBefore(JoinPoint joinPoint) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(request.getRemoteAddr());
        requestInfo.setUrl(request.getRequestURL().toString());
        requestInfo.setHttpMethod(request.getMethod());
        requestInfo.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        requestInfo.setRequestParams(getRequestParam(joinPoint));

        log.info("RequestInfo: {}", requestInfo);


    }

    private Map<String, Object> getRequestParam(JoinPoint joinPoint) {

        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        return buildRequestParam(parameterNames, paramValues);
    }

    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {

        Map<String, Object> requestParams = new HashMap<>();

        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            // 如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename(); // 获取文件名
            }

            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }

    @Around(value = "log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("====================start======================");

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setResult(result);
        resultInfo.setTimeCost(System.currentTimeMillis() - start);

        log.info("ResultInfo: {}", JSONUtil.toJsonPrettyStr(resultInfo));

        log.info("====================end======================");

        return result;
    }


    @Data
    public static class RequestInfo {

        /**
         * 请求IP
         */
        private String ip;

        /**
         * 请求URL
         */
        private String url;

        /**
         * 请求Method
         */
        private String httpMethod;

        /**
         * 访问的方法
         */
        private String classMethod;

        /**
         * 请求参数
         */
        private Object requestParams;
    }

    @Data
    public static class ResultInfo {

        /**
         * 响应结果
         */
        private Object result;

        /**
         * 耗时
         */
        private Long timeCost;
    }
}
