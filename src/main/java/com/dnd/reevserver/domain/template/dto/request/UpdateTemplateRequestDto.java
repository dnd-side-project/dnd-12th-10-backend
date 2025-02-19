package com.dnd.reevserver.domain.template.dto.request;

import java.util.List;

public record UpdateTemplateRequestDto(String userId, Long templateId, String templateName, String content, String description, List<String> categoryNames) {
}
