package com.dnd.reevserver.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

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
