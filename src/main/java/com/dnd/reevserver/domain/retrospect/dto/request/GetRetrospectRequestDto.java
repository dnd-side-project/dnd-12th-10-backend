package com.dnd.reevserver.domain.retrospect.dto.request;

import lombok.Builder;

@Builder
public record GetRetrospectRequestDto(Long retrospectId, String userId, Long groupId) {
}
