package com.dnd.reevserver.domain.team.dto.response;

public record TeamInviteResponseDto(
        boolean valid,
        Long groupId,
        String message
) {}