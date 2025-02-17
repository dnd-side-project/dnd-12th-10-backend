package com.dnd.reevserver.domain.comment.dto.request;

import lombok.Builder;

@Builder
public record AddCommentRequestDto(Long retrospectId, String content) {
}
