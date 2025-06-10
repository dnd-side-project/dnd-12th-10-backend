package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.repository.AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AlertMessageListener {

    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;

    private static final int MAX_CONCURRENT_CONSUME = 10; // 동시에 처리할 수 있는 메시지 수
    private final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_CONSUME);

    // @SqsListener가 붙어 있으면 Spring Cloud AWS가 이 메소드를 자동으로 등록해서
    // SQS에서 메시지가 도착할 때마다 → 이 receive()를 자동 호출
    @SqsListener(value = "${cloud.aws.sqs.queue-name}", factory = "alertSqsListenerContainerFactory")
    public void receive(String messageJson) {
        boolean acquired = false;

        try {
            acquired = semaphore.tryAcquire(2, TimeUnit.SECONDS); // 최대 2초 대기

            if (!acquired) {
                // 너무 많아서 처리를 못 하면, 메시지를 실패로 처리
                throw new RuntimeException("SQS 알림 소비 처리량 초과");
            }

            AlertMessageResponseDto message = objectMapper.readValue(messageJson, AlertMessageResponseDto.class);
            alertRepository.saveAlert(message.userId(), message.messageId(), messageJson);

        } catch (Exception e) {
            throw new RuntimeException("SQS 메시지 수신 처리 중 오류", e);
        } finally {
            if (acquired) {
                semaphore.release();
            }
        }
    }
}
