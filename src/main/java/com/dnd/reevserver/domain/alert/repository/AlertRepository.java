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
    private static final Duration ALERT_TTL = Duration.ofDays(7);
    private final ObjectMapper objectMapper;

    private String getAlertKey(String userId) {
        return "alert:" + userId;
    }

    private String getIsReadKey(String userId) {
        return "alert:" + userId + ":isread";
    }

    public void saveAlert(String userId, String messageId, String messageJson) {
        redisTemplate.opsForList().leftPush(getAlertKey(userId), messageJson);
        redisTemplate.opsForHash().put(getIsReadKey(userId), messageId, "false");
        refreshTtl(userId);
    }

    private void refreshTtl(String userId) {
        redisTemplate.expire(getAlertKey(userId), ALERT_TTL);
        redisTemplate.expire(getIsReadKey(userId), ALERT_TTL);
    }

    public List<String> getAlertsByPage(String userId, int page, int size) {
        int start = page * size;
        int end = start + size - 1;
        return redisTemplate.opsForList().range(getAlertKey(userId), start, end);
    }

    public Object getIsRead(String userId, String messageId) {
        return redisTemplate.opsForHash().get(getIsReadKey(userId), messageId);
    }

    public void markAsRead(String userId, String messageId) {
        redisTemplate.opsForHash().put(getIsReadKey(userId), messageId, "true");
    }

    public long getUnreadCount(String userId) {
        return redisTemplate.opsForHash().values(getIsReadKey(userId)).stream()
                .filter(val -> "false".equals(val))
                .count();
    }

    public long getTotalCount(String userId) {
        return redisTemplate.opsForList().size(getAlertKey(userId));
    }

    public List<String> getAllAlerts(String userId) {
        return redisTemplate.opsForList().range(getAlertKey(userId), 0, -1);
    }

    public void deleteAlert(String userId, String messageId) {
        String alertKey = getAlertKey(userId);
        String isReadKey = getIsReadKey(userId);

        // 리스트에서 해당 메시지를 제거 (O(n))
        List<String> allAlerts = redisTemplate.opsForList().range(alertKey, 0, -1);
        if (allAlerts != null) {
            for (String json : allAlerts) {
                try {
                    AlertMessageResponseDto dto = objectMapper.readValue(json, AlertMessageResponseDto.class);
                    if (dto.messageId().equals(messageId)) {
                        redisTemplate.opsForList().remove(alertKey, 1, json);
                        break;
                    }
                } catch (Exception ignored) {}
            }
        }

        // isread에서도 제거
        redisTemplate.opsForHash().delete(isReadKey, messageId);
    }

    public void deleteAllAlerts(String userId) {
        redisTemplate.delete(getAlertKey(userId));
        redisTemplate.delete(getIsReadKey(userId));
    }
}