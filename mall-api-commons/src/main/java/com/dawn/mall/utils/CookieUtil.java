package com.dawn.mall.utils;

import org.springframework.util.ObjectUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Dawn
 * @Date: 2022/5/14 00:55
 */
public class CookieUtil {

    public static void  set(HttpServletResponse response, String name, String value, int maxAge) {

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static Cookie get(HttpServletRequest request, String name) {

        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        }

        return null;
    }

    /**
     * 将cookie封装成map
     * @param request
     * @return
     */
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {

        Map<String, Cookie> cookieMap = new HashMap<>();

        Cookie[] cookies = request.getCookies();
        if (!ObjectUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }

        return cookieMap;
    }
}
