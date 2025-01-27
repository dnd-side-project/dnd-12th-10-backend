package com.dnd.reevserver.domain.member.service;

import com.dnd.reevserver.global.config.properties.TokenProperties;
import com.dnd.reevserver.domain.member.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenProperties tokenProperties;
    private final RedisRepository redisRepository;

    public String findByUserId(String userId) {
        return redisRepository.findByKey(userId);
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        Duration expiration = Duration.ofDays(tokenProperties.getRefreshTokenExpirationDay());
        redisRepository.save(userId, refreshToken, expiration);
    }

    public void deleteRefreshToken(String userId) {
        redisRepository.delete(userId);
    }
}