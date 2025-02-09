package com.dnd.reevserver.domain.alert.dto.response;

import com.dnd.reevserver.domain.alert.entity.Alert;
import lombok.*;

import java.time.LocalDateTime;

@Getter
public class AlertResponseDto {
    private final Long id;
    private final String message;
    private final boolean isRead;
    private final LocalDateTime createdAt;

    public AlertResponseDto(Alert alert) {
        this.id = alert.getId();
        this.message = alert.getMessage();
        this.isRead = alert.isRead();
        this.createdAt = alert.getCreatedAt();
    }
}