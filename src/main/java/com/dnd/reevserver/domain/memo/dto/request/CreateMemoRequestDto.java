package com.dnd.reevserver.domain.memo.dto.request;

import java.util.List;

public record CreateMemoRequestDto(Long groupId, String title, String content, List<String> categoryNames) {
}
