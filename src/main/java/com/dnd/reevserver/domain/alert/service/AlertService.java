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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertSqsProducer alertSqsProducer;
    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;

    public void sendMessage(String messageId, String userId, String content, LocalDateTime timestamp, Long retrospectId) {
        alertSqsProducer.send(messageId, userId, content, timestamp, retrospectId);
    }

    public AlertListResponseDto getUserAlertList(String userId, int page, int size, boolean onlyUnread) {
        List<String> rawMessages = onlyUnread
                ? alertRepository.getAllAlerts(userId) // 전체 가져와서 필터링
                : alertRepository.getAlertsByPage(userId, page, size);

        long totalCnt = alertRepository.getTotalCount(userId);
        long unreadCnt = alertRepository.getUnreadCount(userId);

        List<AlertMessageResponseDto> allAlerts = rawMessages.stream()
                .map(json -> {
                    try {
                        AlertMessageResponseDto dto = objectMapper.readValue(json, AlertMessageResponseDto.class);
                        Object readValue = alertRepository.getIsRead(userId, dto.messageId());
                        boolean isRead = "true".equals(readValue);
                        return new AlertMessageResponseDto(
                                dto.messageId(), dto.userId(), dto.content(), dto.timestamp(), dto.retrospectId(), isRead
                        );
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<AlertMessageResponseDto> filtered = onlyUnread
                ? allAlerts.stream().filter(a -> !a.isRead()).toList()
                : allAlerts;

        int totalPage = (int) Math.ceil((double) filtered.size() / size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filtered.size());
        List<AlertMessageResponseDto> pageList = (fromIndex < filtered.size())
                ? filtered.subList(fromIndex, toIndex)
                : List.of();

        boolean hasNext = page < totalPage - 1;
        boolean hasPrev = page > 0;

        return new AlertListResponseDto(
                userId,
                pageList,
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