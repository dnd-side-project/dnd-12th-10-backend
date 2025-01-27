package com.dnd.reevserver.domain.group.dto.request;

import lombok.Builder;

@Builder
public record GroupListRequestDto(String userId) {
}
