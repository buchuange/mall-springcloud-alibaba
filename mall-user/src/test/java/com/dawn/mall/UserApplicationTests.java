package com.dawn.mall;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @Author: Dawn
 * @Date: 2022/5/12 16:23
 */
@SpringBootTest
@Slf4j
public class UserApplicationTests {

    private long expiration = 1000 * 60 * 30;

    private String signature = "admin";

    @Test
    public void contextLoads() {

        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();

//简单使用
        log.info("==============="+id);

    }

    @Test
    public void jwt() {

        JwtBuilder jwtBuilder = Jwts.builder();

        String jwtToken = jwtBuilder
                // header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                // payload
                .claim("username", "tom")
                .claim("role", "admin")
                .setSubject("user-login")
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setId(IdUtil.objectId())
                // signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();

        log.info("jwtToken: {}", jwtToken);

    }

    @Test
    public void parse() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJhZG1pbiIsInN1YiI6InVzZXItbG9naW4iLCJleHAiOjE2NTIzNjA0MzksImp0aSI6IjYyN2NmZGYwYTk0MjdiMjFiZWNmNDkxMiJ9.5y9bHh30cRMjGd910HVV3v0blGe_PaNvtFkIPEkyaVw";

        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        log.info(claims.get("username").toString());
        log.info(claims.getId());
    }
}
