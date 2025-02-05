package com.dnd.reevserver.domain.retrospect.dto.request;

import lombok.Builder;

@Builder
public record AddRetrospectRequestDto(String userId, Long groupId,
                                      String title, String content) {
}
