package com.dnd.reevserver.domain.retrospect.dto.response;

import lombok.Builder;

@Builder
public record RetrospectByMemberItemResponseDto(Long retrospectId, String title, String content, String type) {
}
