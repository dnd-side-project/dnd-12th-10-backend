package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertListResponseDto;
import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.repository.AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertSqsProducer alertSqsProducer;
    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;

    private final Sinks.Many<AlertMessageResponseDto> messageSink =
            Sinks.many().multicast().onBackpressureBuffer();

    public void sendMessage(String userId, String content, LocalDateTime timestamp, Long retrospectId) {
        alertSqsProducer.send(userId, content, timestamp, retrospectId);
    }

    public void pushMessageToSink(AlertMessageResponseDto message) {
        messageSink.tryEmitNext(message);
    }

    public Flux<AlertMessageResponseDto> getMessageStream(String userId) {
        return messageSink.asFlux()
                .filter(msg -> msg.userId().equals(userId));
    }

    public AlertListResponseDto getUserAlertList(String userId) {
        List<String> rawMessages = alertRepository.getAlerts(userId);
        List<AlertMessageResponseDto> alerts = rawMessages.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, AlertMessageResponseDto.class);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        long unreadCount = alertRepository.getUnreadCount(userId);

        return new AlertListResponseDto(userId, alerts, unreadCount);
    }
}
