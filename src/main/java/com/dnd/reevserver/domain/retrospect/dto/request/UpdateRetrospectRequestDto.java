package com.dnd.reevserver.domain.retrospect.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateRetrospectRequestDto(Long retrospectId, String title, String content, List<String> categoryNames) {
}
