package com.dnd.reevserver.domain.member.controller;

import com.dnd.reevserver.domain.member.dto.response.ReissueResponseDto;
import com.dnd.reevserver.domain.member.exception.UnauthorizedException;
import com.dnd.reevserver.domain.member.service.AuthService;
import com.dnd.reevserver.global.util.CookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "AT로 진행, Authorization 헤더로 검사함, Bearer AT 형식")
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAuth(@RequestHeader(value = "Authorization", required = false) String authorization) {

        Map<String, Boolean> response = new ConcurrentHashMap<>();

        boolean isAuthenticated = Strings.isNotBlank(authorization) &&
                authorization.startsWith("Bearer ") &&
                authService.checkAuth(authorization);

        response.put("isAuthenticated", isAuthenticated);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "RT로 진행, Redis 안의 RT와 쿠키의 RT를 재갱신함")
    @GetMapping("/reissue")
    public ResponseEntity<Void> reissueToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {

        if (Strings.isEmpty(refreshToken)) {
            throw new UnauthorizedException();
        }

        ReissueResponseDto reissuedToken = authService.reissueToken(refreshToken);

        ResponseCookie refreshCookie = CookieUtils.createReissueCookie(
                "refresh_token",
                reissuedToken.refreshToken(),
                60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, reissuedToken.accessToken())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }
}
