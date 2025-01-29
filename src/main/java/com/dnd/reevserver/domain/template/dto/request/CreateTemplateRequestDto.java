package com.dnd.reevserver.domain.template.dto.request;


public record CreateTemplateRequestDto(String userId, String templateName, String content, String description) {

}
