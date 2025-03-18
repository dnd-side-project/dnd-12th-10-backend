package com.dnd.reevserver.domain.comment.dto.request;

import lombok.Builder;

@Builder
public record UpdateCommentRequestDto(String content) {
}
