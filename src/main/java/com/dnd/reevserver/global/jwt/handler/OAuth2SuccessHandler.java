package com.dnd.reevserver.global.jwt.handler;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.member.service.RefreshTokenService;
import com.dnd.reevserver.global.config.properties.ReevProperties;
import com.dnd.reevserver.global.config.properties.TokenProperties;
import com.dnd.reevserver.global.jwt.provider.JwtProvider;
import com.dnd.reevserver.global.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member oauth2User = (Member) authentication.getPrincipal();

        String userId = oauth2User.getUserId();

        String refreshToken = refreshTokenService.getOrCreateRefreshToken(userId);
        String accessToken = jwtProvider.createAccessToken(userId);

        CookieUtils.addCookie(response, "refresh_token", refreshToken, 60 * 60 * 24 * 7);
        //response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);

        String redirectUrl = getRedirectUrl(request);
        response.sendRedirect(redirectUrl+"?access_token="+accessToken);
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
}
