package com.dnd.reevserver.domain.template.dto.request;


import java.util.List;

public record CreateTemplateRequestDto(String templateName, String content, String description, List<String> categoryNames) {

}
