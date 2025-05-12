package com.dnd.reevserver.domain.memo.dto.request;

import java.util.List;

public record CreateMemoRequestDto(String title, String content, String templateName,
                                   List<String> categoriesName, Long groupId) {
}
