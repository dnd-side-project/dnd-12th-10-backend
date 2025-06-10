package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.repository.AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlertAdminService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final AlertRepository alertRepository;

    private static final String REDIS_FAILED_KEY = "failed:alert-messages";

    public List<AlertMessageResponseDto> getFailedMessages() {
        List<String> messages = redisTemplate.opsForList().range(REDIS_FAILED_KEY, 0, -1);

        if (messages == null || messages.isEmpty()) {
            return List.of();
        }

        return messages.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, AlertMessageResponseDto.class);
                    } catch (Exception e) {
                        // 파싱 실패한 메시지는 무시하거나 로그만 찍고 건너뜀
                        System.err.println("Redis 메시지 파싱 실패: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public String retryMessage(String messageJson) {
        try {
            AlertMessageResponseDto message = objectMapper.readValue(messageJson, AlertMessageResponseDto.class);
            alertRepository.saveAlert(message.userId(), message.messageId(), messageJson);

            redisTemplate.opsForList().remove(REDIS_FAILED_KEY, 1, messageJson);
            return "재처리 성공";
        } catch (Exception e) {
            throw new RuntimeException("재처리 실패: " + e.getMessage(), e);
        }
    }

    public void clearFailedMessages() {
        redisTemplate.delete(REDIS_FAILED_KEY);
    }
}