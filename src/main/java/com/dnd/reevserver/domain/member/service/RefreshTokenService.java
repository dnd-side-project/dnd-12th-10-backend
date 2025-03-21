package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.global.config.properties.TokenProperties;
import com.dnd.reevserver.domain.member.repository.RefreshTokenRepository;
import com.dnd.reevserver.global.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenProperties tokenProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public String findByUserId(String userId) {
        return refreshTokenRepository.findByKey(userId);
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        Duration expiration = Duration.ofDays(tokenProperties.getRefreshTokenExpirationDay());
        refreshTokenRepository.save(userId, refreshToken, expiration);
    }

    public void deleteRefreshToken(String userId) {
        refreshTokenRepository.delete(userId);
    }

    public String getOrCreateRefreshToken(String userId) {
        String existingToken = findByUserId(userId);
        if (existingToken != null) {
            return existingToken; // Redis에 저장된 토큰 재사용
        }

        String newToken = jwtProvider.createRefreshToken(userId);
        saveRefreshToken(userId, newToken); // 새로 생성된 토큰 저장
        return newToken;
    }
}