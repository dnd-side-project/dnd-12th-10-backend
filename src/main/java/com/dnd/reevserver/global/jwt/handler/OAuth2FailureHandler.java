package com.dnd.reevserver.global.jwt.handler;

import com.dnd.reevserver.global.config.properties.ReevProperties;
import com.dnd.reevserver.global.config.properties.TokenProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ReevProperties reevProperties;
    private final TokenProperties tokenProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        request.getSession().removeAttribute(tokenProperties.getQueryParam());
        response.sendRedirect(reevProperties.getFrontUrl().get(0) + "/login/failure");
    }
}