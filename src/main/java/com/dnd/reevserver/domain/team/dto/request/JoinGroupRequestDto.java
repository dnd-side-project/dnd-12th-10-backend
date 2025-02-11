package com.dnd.reevserver.domain.team.dto.request;

import lombok.Builder;

@Builder
public record JoinGroupRequestDto(String userId, Long groupId) {
}
