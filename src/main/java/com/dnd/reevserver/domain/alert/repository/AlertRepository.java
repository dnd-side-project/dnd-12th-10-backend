package com.dnd.reevserver.domain.alert.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlertRepository {
    private final StringRedisTemplate redisTemplate;

    private String getRedisKey(String userId) {
        return "alert:user:" + userId;
    }

    public void saveAlert(String userId, String message) {
        String key = getRedisKey(userId);
        redisTemplate.opsForList().leftPush(key, message);
        redisTemplate.expire(key, Duration.ofDays(7));
    }

    public List<String> getAlerts(String userId) {
        return redisTemplate.opsForList().range(getRedisKey(userId), 0, -1);
    }

    public long getUnreadCount(String userId) {
        return redisTemplate.opsForList().size(getRedisKey(userId));
    }
}

