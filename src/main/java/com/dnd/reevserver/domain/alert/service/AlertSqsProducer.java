package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AlertSqsProducer {

    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;

    private static final int MAX_CONCURRENT_SENDS = 20; // 동시에 보낼 수 있는 최대 개수
    private final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_SENDS);

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueUrl;

    public void send(String messageId, String userId, String content, LocalDateTime timestamp, Long retrospectId) {
        try {
            boolean acquired = semaphore.tryAcquire(2, TimeUnit.SECONDS); // 대기 최대 2초

            if (!acquired) {
                throw new RuntimeException("SQS 전송 대기열 초과: 메시지 전송을 건너뜁니다.");
            }

            AlertMessageResponseDto dto = new AlertMessageResponseDto(messageId, userId, content, timestamp, retrospectId, false);
            String json = objectMapper.writeValueAsString(dto);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .build();

            sqsAsyncClient.sendMessage(request)
                    .whenComplete((resp, ex) -> {
                        semaphore.release(); // 완료 후 반드시 해제
                        if (ex != null) {
                            System.err.println("SQS 전송 실패: " + ex.getMessage());
                        }
                    });

        } catch (Exception e) {
            semaphore.release(); // 예외 시에도 해제
            throw new RuntimeException("SQS 메시지 전송 중 오류 발생", e);
        }
    }
}
