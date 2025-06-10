package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.repository.AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertMessageListener {

    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    private static final String REDIS_FAILED_KEY = "failed:alert-messages";
    private static final int MAX_CONCURRENT_CONSUME = 10;
    private final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_CONSUME);

    @SqsListener(value = "${cloud.aws.sqs.queue-name}", factory = "alertSqsListenerContainerFactory")
    public void receive(String messageJson) {
        log.info("AlertMessageListener receive 실행");
        boolean acquired = false;

        try {
            acquired = semaphore.tryAcquire(2, TimeUnit.SECONDS);
            if (!acquired) {
                // 처리량 초과 → Redis에 저장 후 종료
                backupToRedis(messageJson, "세마포어 획득 실패");
                return;
            }

            AlertMessageResponseDto message = objectMapper.readValue(messageJson, AlertMessageResponseDto.class);
            alertRepository.saveAlert(message.userId(), message.messageId(), messageJson);

        } catch (Exception e) {
            // 예외 발생 시 메시지 원본을 Redis에 저장
            backupToRedis(messageJson, e.getMessage());
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }
    }

    private void backupToRedis(String messageJson, String reason) {
        try {
            ListOperations<String, String> ops = redisTemplate.opsForList();
            ops.leftPush(REDIS_FAILED_KEY, messageJson);
            System.err.println("실패한 메시지를 Redis에 저장했습니다: " + reason);
        } catch (Exception redisEx) {
            System.err.println("Redis 저장도 실패했습니다: " + redisEx.getMessage());
        }
    }
}