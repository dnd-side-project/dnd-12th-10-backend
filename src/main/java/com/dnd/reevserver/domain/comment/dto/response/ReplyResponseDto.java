package com.dnd.reevserver.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record ReplyResponseDto(Long commentId, String userId, Long retrospectId,
                               String content, String nickName,String timeMessage) {
}
