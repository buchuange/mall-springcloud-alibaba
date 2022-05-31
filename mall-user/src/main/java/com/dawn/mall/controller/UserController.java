package com.dawn.mall.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.dawn.mall.consts.CookieConsts;
import com.dawn.mall.consts.RedisConsts;
import com.dawn.mall.domain.User;
import com.dawn.mall.dto.UserLoginDTO;
import com.dawn.mall.dto.UserRegisterDTO;
import com.dawn.mall.service.UserService;
import com.dawn.mall.utils.CookieUtil;
import com.dawn.mall.utils.MallUtil;
import com.dawn.mall.utils.RedisUtil;
import com.dawn.mall.utils.ResultVoUtil;
import com.dawn.mall.vo.ResultVo;
import com.dawn.mall.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author: Dawn
 * @Date: 2022/5/13 00:40
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/user/register")
    public ResultVo<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {

        User user = userRegisterDTO.convertToUser();

        return userService.register(user);
    }


    @PostMapping("/user/login")
    public ResultVo<UserVo> login(@Valid @RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {

        UserVo user = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());

        // 生成token令牌
        String token = IdUtil.simpleUUID();

        // 设置token到cookie
        CookieUtil.set(response, CookieConsts.TOKEN, token, CookieConsts.EXPIRE);

        // 设置token到redis中
        RedisUtil.set(String.format(RedisConsts.TOKEN_PREFIX, token), JSONUtil.toJsonStr(user), RedisConsts.EXPIRE);

        return ResultVoUtil.success(user);
    }

    @GetMapping("/user")
    public ResultVo<User> userInfo(HttpServletRequest request) {

        User user = MallUtil.getUserInfo(request);

        return ResultVoUtil.success(user);
    }



    @PostMapping("/user/logout")
    public ResultVo<String> logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie cookie = CookieUtil.get(request, CookieConsts.TOKEN);

        CookieUtil.set(response, CookieConsts.TOKEN, null, 0);

        assert cookie != null;
        RedisUtil.delete(String.format(RedisConsts.TOKEN_PREFIX, cookie.getValue()));

        return ResultVoUtil.success("退出成功");
    }
}
