package com.dnd.reevserver.domain.retrospect.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record AddRetrospectRequestDto(Long groupId, String title, String content, List<String> categoryNames) {
}
