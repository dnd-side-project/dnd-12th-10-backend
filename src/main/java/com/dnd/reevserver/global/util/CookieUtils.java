package com.dnd.reevserver.global.util;

import org.springframework.http.ResponseCookie;

public class CookieUtils {
    private CookieUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
    }

    public static ResponseCookie createCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
