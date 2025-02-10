package com.dnd.reevserver.domain.team.dto.response;

import lombok.Builder;

@Builder
public record JoinGroupResponseDto(String userId, Long groupId) {
}
