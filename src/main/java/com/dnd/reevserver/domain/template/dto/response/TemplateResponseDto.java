package com.dnd.reevserver.domain.template.dto.response;

import com.dnd.reevserver.domain.category.entity.TemplateCategory;
import com.dnd.reevserver.domain.template.entity.Template;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TemplateResponseDto {
    private final Long templateId;
    private final String templateName;
    private final String content;
    private final boolean isPublic;
    private final String userId;
    private final List<String> categories;

    public TemplateResponseDto(Template template) {
        this.templateId = template.getTemplateId();
        this.templateName = template.getTemplateName();
        this.content = template.getContent();
        this.isPublic = template.isPublic();
        this.userId = template.isPublic() ? "public" : template.getMember().getUserId();

        // 스트림 API를 사용하여 categories 리스트 변환 & 불변 리스트로 저장
        this.categories = List.copyOf(
                template.getTemplateCategories().stream()
                        .map(tc -> tc.getCategory().getCategoryName())
                        .toList()
        );
    }
}

