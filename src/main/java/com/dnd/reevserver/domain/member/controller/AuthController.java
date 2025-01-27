package com.dnd.reevserver.domain.member.controller;

import com.dnd.reevserver.domain.member.dto.response.ReissueResponseDto;
import com.dnd.reevserver.domain.member.exception.UnauthorizedException;
import com.dnd.reevserver.domain.member.service.AuthService;
import com.dnd.reevserver.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
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
public class AuthController {
    private final AuthService authService;

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAuth(@RequestHeader(value = "Authorization", required = false) String authorization) {

        Map<String, Boolean> response = new ConcurrentHashMap<>();

        boolean isAuthenticated = Strings.isNotBlank(authorization) &&
                authorization.startsWith("Bearer ") &&
                authService.checkAuth(authorization);

        response.put("isAuthenticated", isAuthenticated);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reissue")
    public ResponseEntity<Void> reissueToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {

        if (Strings.isEmpty(refreshToken)) {
            throw new UnauthorizedException();
        }

        ReissueResponseDto reissuedToken = authService.reissueToken(refreshToken);

        ResponseCookie refreshCookie = CookieUtils.createCookie(
                "refresh_token",
                reissuedToken.getRefreshToken(),
                60 * 60 * 24 * 7);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, reissuedToken.getAccessToken())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }
}
