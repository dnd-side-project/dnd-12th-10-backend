package com.dnd.reevserver.domain.member.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class RefreshTokenRepository {

    @Qualifier("redisStringTemplate") // 특정 RedisTemplate을 지정
    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String findByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void save(String key, String value, Duration expiration) {
        redisTemplate.opsForValue().set(key, value, expiration);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
