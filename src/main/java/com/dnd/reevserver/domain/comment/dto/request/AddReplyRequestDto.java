package com.dnd.reevserver.domain.comment.dto.request;

import lombok.Builder;

@Builder
public record AddReplyRequestDto(String userId, Long retrospectId, Long commentId, String content) {
}
