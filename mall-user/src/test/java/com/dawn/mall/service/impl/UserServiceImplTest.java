package com.dawn.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.UserApplicationTests;
import com.dawn.mall.domain.User;
import com.dawn.mall.service.UserService;
import com.dawn.mall.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserServiceImplTest extends UserApplicationTests {

    @Resource
    private UserService userService;

    @Test
    public void login() {

        UserVo user = userService.login("admin", "123456");

        log.info("user: {}", JSONUtil.toJsonPrettyStr(user));

        Assertions.assertNotNull(user);
    }
}