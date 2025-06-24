package com.dnd.reevserver.domain.memo.dto.request;

import java.util.List;

public record UpdateMemoRequestDto(Long memoId, Long groupId, String title, String content, List<String> categoryNames) {
}
