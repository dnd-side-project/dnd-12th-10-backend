package com.dnd.reevserver.global.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;


public class CookieUtils {
    private CookieUtils() {
        throw new IllegalStateException("Utility class");
    }

     public static ResponseCookie createReissueCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .secure(true)
                .sameSite("None")
                .httpOnly(false)
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .sameSite("None")
                .secure(true)
                .httpOnly(false)
                .path("/")
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .sameSite("None")
                .secure(true)
                .httpOnly(false) // 개발 환경에서는 false (운영 환경에서는 true)
                .path("/")
                .maxAge(0) // 쿠키 즉시 만료
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

