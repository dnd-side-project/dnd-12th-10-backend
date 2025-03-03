package com.dnd.reevserver.domain.memo.dto.response;

import lombok.Builder;

@Builder
public record MemoResponseDto(Long memoId, String userId, String title, String content, String templateName){
}
