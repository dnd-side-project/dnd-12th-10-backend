package com.dnd.reevserver.domain.template.dto.response;

import com.dnd.reevserver.domain.template.entity.Template;
import lombok.Builder;
import java.util.List;

@Builder
public record TemplateResponseDto(Long templateId, String templateName, String content, String preset,
                                  boolean isPublic, String userId, List<String> categories){

//    public TemplateResponseDto(Template template) {
//        this.templateId = template.getTemplateId();
//        this.templateName = template.getTemplateName();
//        this.content = template.getContent();
//        this.isPublic = template.isPublic();
//        this.preset = template.getPreset();
//        this.userId = template.isPublic() ? "public" : template.getMember().getUserId();
//
//        // 스트림 API를 사용하여 categories 리스트 변환 & 불변 리스트로 저장
//        this.categories = List.copyOf(
//                template.getTemplateCategories().stream()
//                        .map(tc -> tc.getCategory().getCategoryName())
//                        .toList()
//        );
//    }
}

