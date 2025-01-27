package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.domain.member.dto.response.ReissueResponseDto;
import com.dnd.reevserver.domain.member.exception.UnauthorizedException;
import com.dnd.reevserver.global.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public ReissueResponseDto reissueToken(String refreshToken) {

        String userId = jwtProvider.validateToken(refreshToken);
        String existsToken = refreshTokenService.findByUserId(userId);

        if (!refreshToken.equals(existsToken)) {
            refreshTokenService.deleteRefreshToken(userId);
            throw new UnauthorizedException();
        }

        String newAccessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);

        refreshTokenService.saveRefreshToken(userId, newRefreshToken);

        return new ReissueResponseDto("Bearer " + newAccessToken, newRefreshToken);
    }

    public boolean checkAuth(String authorization) {

        String accessToken = authorization.substring(7);
        String validate = jwtProvider.validateToken(accessToken);

        return Strings.isNotBlank(validate);
    }
}
