package com.dnd.reevserver.global.jwt.handler;

import com.dnd.reevserver.domain.member.repository.MemberRepository;
import com.dnd.reevserver.domain.member.service.RefreshTokenService;
import com.dnd.reevserver.global.config.properties.TokenProperties;
import com.dnd.reevserver.global.jwt.provider.JwtProvider;
import com.dnd.reevserver.global.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class OAuth2LogoutHandler implements LogoutHandler {

    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String refreshToken = extractRefreshToken(request.getCookies());

        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String userId = jwtProvider.validateToken(refreshToken);
        if (!memberRepository.existsByUserId(userId)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 삭제할 쿠키 생성
        CookieUtils.deleteCookie(response, "refresh_token");
        response.addHeader(HttpHeaders.AUTHORIZATION, "");
        refreshTokenService.deleteRefreshToken(userId);
    }

    /**
     * 쿠키에서 Refresh Token을 추출합니다.
     *
     * @param cookies 모든 쿠키
     * @return Refresh Token
     */
    public String extractRefreshToken(Cookie[] cookies) {

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
