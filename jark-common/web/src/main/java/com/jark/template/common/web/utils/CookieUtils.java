package com.jark.template.common.web.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Cookie 工具类
 */
@Slf4j
public final class CookieUtils {

    private CookieUtils() {
    }

    public static void setCookie(final HttpServletResponse response, final String name, final String value) {
        setCookie(response, name, value, "/");
    }

    public static void setCookie(final HttpServletResponse response, final String name, final String value, final String path) {
        setCookie(response, name, value, path, 60 * 60 * 24);
    }

    public static void setCookie(final HttpServletResponse response, final String name, final String value, final String path, final int maxAge) {
        final Cookie cookie = new Cookie(name, URLEncoder.encode(value, StandardCharsets.UTF_8));
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static Optional<String> getCookie(final HttpServletRequest request, String name) {
        final Cookie[] cookies = request.getCookies();
        if (ObjectUtil.isNotEmpty(cookies)) {
            for (final Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    return Optional.of(URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8));
                }
            }
        }
        return Optional.empty();
    }

}


