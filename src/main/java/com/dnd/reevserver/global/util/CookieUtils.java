package com.dnd.reevserver.global.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;


public class CookieUtils {
    private CookieUtils() {
        throw new IllegalStateException("Utility class");
    }

    // ✅ `refresh_token`을 만들 때 사용하는 메서드 (토큰 저장)
    public static ResponseCookie createReissueCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .secure(true) // 개발 환경에서는 secure=false (운영 환경에서는 true로 변경 가능)
                .sameSite("None")
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    // ✅ 기존의 `addCookie()`를 `ResponseCookie` 기반으로 변경 (일관성 유지)
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .sameSite("None") // 크로스 사이트에서도 쿠키 유지
                .secure(true) // 개발 환경에서는 false, 운영에서는 true로 변경 가능
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // ✅ 쿠키를 삭제할 때도 `ResponseCookie`를 사용 (일관성 유지)
    public static void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .sameSite("None") // 크로스 사이트에서도 삭제되도록 설정
                .secure(true) // 개발 환경에서는 false (운영 환경에서는 true)
                .httpOnly(true) // JavaScript에서 접근 방지
                .path("/")
                .maxAge(0) // 쿠키 즉시 만료
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

