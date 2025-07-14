package com.dnd.reevserver.domain.template.dto.request;

import java.util.List;

public record UpdateTemplateRequestDto(Long templateId, String templateName, String content, String preset, List<String> categoryNames, String description) {
}
