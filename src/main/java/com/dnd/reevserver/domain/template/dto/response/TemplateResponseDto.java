package com.dnd.reevserver.domain.template.dto.response;

import com.dnd.reevserver.domain.template.entity.Template;
import lombok.Builder;
import java.util.List;

@Builder
public record TemplateResponseDto(Long templateId, String templateName, String description, String content, String preset,
                                  boolean isPublic, String userId, List<String> categories){
}

