package com.dawn.mall.filter;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.consts.CookieConsts;
import com.dawn.mall.consts.MallConsts;
import com.dawn.mall.consts.RedisConsts;
import com.dawn.mall.enums.ResultEnum;
import com.dawn.mall.utils.RedisUtil;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Dawn
 * @Date: 2022/5/15 02:08
 */
@Component
@Slf4j
public class GatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // 获取请求的uri
        String path = request.getURI().getPath();

        ServerHttpRequest req = request.mutate()
                .header(MallConsts.AUTHORIZE_KEY, MallConsts.AUTHORIZE_VALUE).build();

        if (path.startsWith("/user/login") || path.startsWith("/user/register")
                || path.startsWith("/categories") || path.startsWith("/products")
                || path.startsWith("/products/*")   || path.startsWith("/pay/notify")) {

            return chain.filter(exchange.mutate().request(req.mutate().build()).build());
        }

        HttpCookie cookie = request.getCookies().getFirst(CookieConsts.TOKEN);

        if (ObjectUtils.isEmpty(cookie)) {
            log.warn("【登录校验】Cookie中查不到token");
            return unAuthorize(exchange);

        }

        // redis中查询
        String key = String.format(RedisConsts.TOKEN_PREFIX, cookie.getValue());
        String tokenValue = RedisUtil.get(key);

        if (!StringUtils.hasLength(tokenValue)) {
            log.warn("【登录校验】Redis中查不到token");
            return unAuthorize(exchange);
        }

        // 刷新过期时间
        RedisUtil.expire(key, RedisConsts.EXPIRE);

        return chain.filter(exchange.mutate().request(req.mutate().build()).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public static Mono<Void> unAuthorize(ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        ResultVo<Object> error = ResultVoUtil.error(ResultEnum.USER_NOT_LOGIN);

        byte[] bits = JSONUtil.toJsonStr(error).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}