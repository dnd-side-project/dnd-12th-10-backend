package com.dnd.reevserver.domain.comment.dto.request;

import lombok.Builder;

@Builder
public record AddCommentRequestDto(String userId, Long retrospectId, String content) {
}
