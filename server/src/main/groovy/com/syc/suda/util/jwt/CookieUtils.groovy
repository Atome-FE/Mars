package com.syc.suda.util.jwt

import org.springframework.util.StringUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

class CookieUtils {

    /**
     * 向cookie存储新字段（使用 URL Encode 编码）
     */
    static void setEncoded(String name, String content, int expiredIn, boolean isHttpOnly) {
        try {
            setRaw(name, URLEncoder.encode(content, "UTF-8"), expiredIn, isHttpOnly)
        } catch (UnsupportedEncodingException ignored) {
        }
    }

    /**
     * 如果key != null,清除指定key的cookie,如果key为null,清除所有cookie
     */
    static void clean(String key) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes()
        HttpServletRequest request = attributes.getRequest()
        Cookie[] cookies = request.getCookies()
        if (cookies != null) {
            if (!StringUtils.isEmpty(key)) {
                setRaw(key, "", 0, true)
            } else {
                cookies.each {
                    setEncoded(it.getName(), '', 0, true)
                }
            }
        }
    }
    /**
     * 向cookie存储新字段（不做编解码处理）
     */
    static void setRaw(String name, String value, int expiredIn, boolean isHttpOnly) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes()
        if (attributes != null) {
            Cookie cookie = new Cookie(name, value)
            cookie.setPath("/")
            cookie.setMaxAge(expiredIn)
            cookie.setHttpOnly(isHttpOnly)
            attributes.getResponse().addCookie(cookie)
        }
    }

    /**
     * 从cookie中取字段（不做编解码处理）
     */
    static String getRaw(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes()
        if (attributes != null) {
            Cookie[] cookies = attributes.getRequest().getCookies()
            if (cookies != null) {
                /*
                Optional<Cookie> first = Arrays.stream(cookies).filter(e -> name.equalsIgnoreCase(e.getName())).findFirst()
                if (first.isPresent()) {
                    return first.get().getValue()
                }
                */
                Cookie cookie = cookies.find {
                    return name.equalsIgnoreCase(it.getName())
                }
                if (cookie) {
                    return cookie.getValue()
                }
            }
        }
        return null
    }
}
