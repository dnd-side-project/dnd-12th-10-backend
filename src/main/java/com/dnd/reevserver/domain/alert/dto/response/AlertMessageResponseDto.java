package com.dnd.reevserver.domain.alert.dto.response;

import java.time.LocalDateTime;

public record AlertMessageResponseDto(
        String userId,
        String content,
        LocalDateTime timestamp,
        Long retrospectId
) {}
