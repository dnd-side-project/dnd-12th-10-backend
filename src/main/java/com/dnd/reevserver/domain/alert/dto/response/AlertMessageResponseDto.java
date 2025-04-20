package com.dnd.reevserver.domain.alert.dto.response;

import java.time.LocalDateTime;

public record AlertMessageResponseDto(
        String messageId,
        String userId,
        String content,
        LocalDateTime timestamp,
        Long retrospectId,
        boolean isRead
) {}
