package com.dnd.reevserver.domain.retrospect.dto.request;

import lombok.Builder;

@Builder
public record GetRetrospectRequestDto(Long retrospectId, Long groupId) {
}
