package com.dnd.reevserver.global.jwt.handler;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.RefreshTokenService;
import com.dnd.reevserver.global.config.properties.ReevProperties;
import com.dnd.reevserver.global.config.properties.TokenProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ReevProperties reevProperties;
    private final RefreshTokenService refreshTokenService;
    private final TokenProperties tokenProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member oauth2User = (Member) authentication.getPrincipal();

        String userId = oauth2User.getUserId();

        String refreshToken = refreshTokenService.getOrCreateRefreshToken(userId);

        ResponseCookie refreshCookie = createCookie(tokenProperties.getRefreshTokenName(), refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        String redirectUrl = getRedirectUrl(request);
        response.sendRedirect(redirectUrl);
    }

    public String getRedirectUrl(HttpServletRequest request) {
        String baseUrl = reevProperties.getFrontUrl().get(0) + "/login/success";

        HttpSession session = request.getSession();
        String redirectParam = (String) session.getAttribute(tokenProperties.getQueryParam());
        session.removeAttribute(tokenProperties.getQueryParam());

        return (redirectParam != null)
                ? baseUrl + "?redirect=" + redirectParam
                : baseUrl;
    }

    private ResponseCookie createCookie(String name, String value) {

        return ResponseCookie.from(name, value)
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .path("/")
                .maxAge(604800)
                .build();
    }
}
