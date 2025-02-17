package com.dnd.reevserver.domain.comment.dto.request;

import lombok.Builder;

@Builder
public record AddReplyRequestDto(Long retrospectId, String content) {
}
