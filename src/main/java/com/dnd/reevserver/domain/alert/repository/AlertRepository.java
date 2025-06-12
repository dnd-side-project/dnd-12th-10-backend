package com.dnd.reevserver.domain.alert.repository;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlertRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final Duration READ_TTL = Duration.ofDays(7);
    private static final String READ_MARK_PREFIX = "read:";
    private static final String ALERT_MARK_PREFIX = "alert:";

    private String getAlertKey(String userId) {
        return ALERT_MARK_PREFIX + userId;
    }

    private String getReadKey(String userId, String messageId) {
        return ALERT_MARK_PREFIX + userId + ":" + READ_MARK_PREFIX + messageId;
    }

    public List<String> getAlertsByPage(String userId, int page, int size, long totalCnt) {
        int start = page * size;
        int end = Math.min(start + size - 1, (int) totalCnt - 1);
        return redisTemplate.opsForList().range(getAlertKey(userId), start, end);
    }

    public boolean isRead(String userId, String messageId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getReadKey(userId, messageId)));
    }

    public void markAsRead(String userId, String messageId) {
        redisTemplate.opsForValue().set(getReadKey(userId, messageId), "true", READ_TTL);
    }

    public long getUnreadCount(String userId) {
        List<String> allAlerts = redisTemplate.opsForList().range(getAlertKey(userId), 0, -1);
        if (allAlerts == null) return 0;

        return allAlerts.stream().filter(json -> {
            try {
                AlertMessageResponseDto dto = objectMapper.readValue(json, AlertMessageResponseDto.class);
                return !isRead(userId, dto.messageId());
            } catch (Exception e) {
                return false;
            }
        }).count();
    }

    public long getTotalCount(String userId) {
        return redisTemplate.opsForList().size(getAlertKey(userId));
    }

    public void deleteAlert(String userId, String messageId) {
        String alertKey = getAlertKey(userId);

        List<String> allAlerts = redisTemplate.opsForList().range(alertKey, 0, -1);
        if (allAlerts != null) {
            for (String json : allAlerts) {
                try {
                    AlertMessageResponseDto dto = objectMapper.readValue(json, AlertMessageResponseDto.class);
                    if (dto.messageId().equals(messageId)) {
                        redisTemplate.opsForList().remove(alertKey, 1, json);
                        redisTemplate.delete(getReadKey(userId, messageId));
                        break;
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    public void deleteAllAlerts(String userId) {
        String alertKey = getAlertKey(userId);
        List<String> allAlerts = redisTemplate.opsForList().range(alertKey, 0, -1);
        if (allAlerts != null) {
            for (String json : allAlerts) {
                try {
                    AlertMessageResponseDto dto = objectMapper.readValue(json, AlertMessageResponseDto.class);
                    redisTemplate.delete(getReadKey(userId, dto.messageId()));
                } catch (Exception ignored) {}
            }
        }
        redisTemplate.delete(alertKey);
    }
}
