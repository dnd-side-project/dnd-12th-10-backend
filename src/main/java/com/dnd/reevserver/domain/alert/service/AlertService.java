package com.dnd.reevserver.domain.alert.service;

import com.dnd.reevserver.domain.alert.dto.response.AlertListResponseDto;
import com.dnd.reevserver.domain.alert.dto.response.AlertMessageResponseDto;
import com.dnd.reevserver.domain.alert.repository.AlertRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertSqsProducer alertSqsProducer;
    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;

    public void sendMessage(String messageId, String userId, String content, LocalDateTime timestamp, Long retrospectId) {
        alertSqsProducer.send(messageId, userId, content, timestamp, retrospectId);
    }

    public AlertListResponseDto getUserAlertList(String userId, int page, int size) {
        long totalCnt = alertRepository.getTotalCount(userId);
        long unreadCnt = alertRepository.getUnreadCount(userId);

        List<String> rawMessages = alertRepository.getAlertsByPage(userId, page, size, totalCnt);

        List<AlertMessageResponseDto> allAlerts = rawMessages.stream()
                .map(json -> {
                    try {
                        AlertMessageResponseDto dto = objectMapper.readValue(json, AlertMessageResponseDto.class);
                        boolean isRead = alertRepository.isRead(userId, dto.messageId());
                        return new AlertMessageResponseDto(
                                dto.messageId(), dto.userId(), dto.content(), dto.timestamp(), dto.retrospectId(), isRead
                        );
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        int totalPage = (int) Math.ceil((double) totalCnt / size);
        boolean hasNext = page < totalPage - 1;
        boolean hasPrev = page > 0;

        return new AlertListResponseDto(
                userId,
                allAlerts,
                totalCnt,
                unreadCnt,
                page,
                size,
                totalPage,
                hasNext,
                hasPrev
        );
    }

    public void updateRead(String userId, String messageId) {
        alertRepository.markAsRead(userId, messageId);
    }

    public void deleteAlert(String userId, String messageId) {
        alertRepository.deleteAlert(userId, messageId);
    }

    public void deleteAllAlerts(String userId) {
        alertRepository.deleteAllAlerts(userId);
    }
}