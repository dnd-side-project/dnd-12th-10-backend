package com.dnd.reevserver.domain.retrospect.dto.request;

import lombok.Builder;

@Builder
public record UpdateRetrospectRequestDto(Long retrospectId, String userId,
                                         String title, String content) {
}
