package com.dnd.reevserver.domain.template.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTemplateRequestDto {
    private Long templateId;
    private String templateName;
    private String content;
}
