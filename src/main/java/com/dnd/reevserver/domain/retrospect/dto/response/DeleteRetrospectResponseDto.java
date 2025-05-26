package com.dnd.reevserver.domain.retrospect.dto.response;

import lombok.Builder;

@Builder
public record DeleteRetrospectResponseDto(Long retrospectId) {
}
