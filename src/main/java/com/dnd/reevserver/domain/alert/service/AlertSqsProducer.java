package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;

@Component
public class AlertSqsProducer {
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;

    public AlertSqsProducer(@Qualifier("alertSqsClient") SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.objectMapper = objectMapper;
    }

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueUrl;

    public void send(String messageId, String userId, String content, LocalDateTime timestamp, Long retrospectId) {
        try {
            AlertMessageResponseDto dto = new AlertMessageResponseDto(messageId, userId, content, timestamp, retrospectId, false);
            String json = objectMapper.writeValueAsString(dto);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(json)
                    .build();

            sqsAsyncClient.sendMessage(request);
        } catch (Exception e) {
            throw new RuntimeException("SQS 메시지 전송 중 오류 발생", e);
        }
    }
}