package com.dnd.reevserver.domain.alert.dto.response;

import java.util.List;

public record AlertListResponseDto(
        String userId,
        List<AlertMessageResponseDto> alerts,
        long unreadCnt
) {}