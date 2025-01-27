package com.dnd.reevserver.domain.template.dto.response;

import com.dnd.reevserver.domain.template.entity.Template;
import lombok.Getter;

@Getter
public class TemplateResponseDto {
    private final Long templateId;
    private final String templateName;
    private final String content;
    private final boolean isPublic;
    private final String userId;

    public TemplateResponseDto(Template template) {
        this.templateId = template.getTemplateId();
        this.templateName = template.getTemplateName();
        this.content = template.getContent();
        this.isPublic = template.isPublic();
        if(template.isPublic()){
            this.userId = "public";
        }
        else this.userId = template.getMember().getUserId();
    }
}
