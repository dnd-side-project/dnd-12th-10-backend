package com.dnd.reevserver.domain.alert.dto.response;

import java.util.List;

public record AlertListResponseDto(
        String userId,
        List<AlertMessageResponseDto> alerts,
        long totalCnt,
        long unreadCnt,
        int page,
        int size,
        int totalPage,
        boolean hasNext,
        boolean hasPrev
) {}