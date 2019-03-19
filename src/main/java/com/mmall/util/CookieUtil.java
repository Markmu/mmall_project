package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private static final String COOKIE_DOMAIN = ".mmall.com";

    private static final String COOKIE_NAME = "mmall_login_token";


    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        //  设置在根目录
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // 设置cookie有效时间，如果不设置则不会写入硬盘，而是写入内存只对当前页面有效。
        // -1 代表永久
        cookie.setMaxAge(60 * 60 * 24 * 365);
        log.info("write cookie name:{} , value:{}", COOKIE_NAME, token);
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            log.info("Read cookie name:{} , cookie value:{}", c.getName(), c.getValue());
            if (StringUtils.equals(COOKIE_NAME, c.getName())) {
                log.info("Return cookie name:{} , cookie value:{}", c.getName(), c.getValue());
                return c.getValue();
            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie c : cookies) {
            log.info("Read cookie name:{} , cookie value:{}", c.getName(), c.getValue());
            if (StringUtils.equals(COOKIE_NAME, c.getName())) {
                log.info("Delete cookie name:{} , cookie value:{}", c.getName(), c.getValue());
                c.setDomain(COOKIE_DOMAIN);
                c.setPath("/");
                // 有效期设置为0，浏览其会自动删除该cookie
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }
    }

}
