package com.dnd.reevserver.domain.retrospect.dto.request;

import lombok.Builder;

@Builder
public record GetAllGroupRetrospectRequestDto(String userId, Long groupId) {
}
