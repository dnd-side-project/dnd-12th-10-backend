package com.dnd.reevserver.domain.template.dto.request;

public record UpdateTemplateRequestDto(Long templateId, String templateName, String content, String description) {
}
