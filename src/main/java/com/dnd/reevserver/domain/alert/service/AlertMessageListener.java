package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.repository.AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertMessageListener {

    private final AlertRepository alertRepository;
    private final AlertService alertService;
    private final ObjectMapper objectMapper;

    @SqsListener("${cloud.aws.sqs.queue-name}")
    public void receive(String messageJson) {
        try {
            AlertMessageResponseDto message = objectMapper.readValue(messageJson, AlertMessageResponseDto.class);
            alertRepository.saveAlert(message.userId(), messageJson);
            alertService.pushMessageToSink(message);
        } catch (Exception e) {
            // log.warn("알림 메시지 처리 실패", e);
        }
    }
}