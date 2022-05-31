package com.dawn.mall.utils;

import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Dawn
 * @Date: 2022/5/12 22:47
 */
public class JwtUtil {

    /**
     * 自定义秘钥
     * */
    private static final String JWT_KEY = "Irving" ;

    /**
     * jwtToken的默认有效时间 单位毫秒
     * */
    private static final int EXPIRE_TIME = 1000 * 60 * 30;

    /**
     * 生成token header.payload.singature
     */
    public static String generalToken(Map<String, Object> map) {

        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(IdUtil.simpleUUID())
                .setSubject("user-login")
                .setIssuedAt(new Date())
                .setIssuer("星辰破晓")
                // header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // payload
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                // signature
                .signWith(SignatureAlgorithm.HS256, generalKey());

        return jwtBuilder.compact();
    }

    /**
     * 生成加密 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JwtUtil.JWT_KEY.getBytes());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 从令牌中获取数据,就是payLoad部分存放的数据。如果jwt被改，该函数会直接抛出异常
     * @param token  令牌
     * */
    public static Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody() ;
    }
}
