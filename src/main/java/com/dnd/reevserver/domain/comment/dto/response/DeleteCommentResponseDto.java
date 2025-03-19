package com.dnd.reevserver.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record DeleteCommentResponseDto(Long commentId) {

}
