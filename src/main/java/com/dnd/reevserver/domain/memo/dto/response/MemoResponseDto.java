package com.dnd.reevserver.domain.memo.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record MemoResponseDto(Long memoId, String userId, String title, String updateTime,
                              String content, List<String> categoryNames, Long groupId, Long templateId){
}
