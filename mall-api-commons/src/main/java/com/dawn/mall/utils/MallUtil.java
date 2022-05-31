package com.dawn.mall.utils;

import cn.hutool.json.JSONUtil;
import com.dawn.mall.consts.CookieConsts;
import com.dawn.mall.consts.RedisConsts;
import com.dawn.mall.domain.User;
import com.dawn.mall.vo.UserVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Dawn
 * @Date: 2022/5/15 20:35
 */
public class MallUtil {

    public static User getUserInfo(HttpServletRequest request) {

        Cookie cookie = CookieUtil.get(request, CookieConsts.TOKEN);

        assert cookie != null;
        String value = RedisUtil.get(String.format(RedisConsts.TOKEN_PREFIX, cookie.getValue()));

        UserVo userVo = JSONUtil.toBean(value, UserVo.class);

        return userVo.convertToUser();
    }
}
