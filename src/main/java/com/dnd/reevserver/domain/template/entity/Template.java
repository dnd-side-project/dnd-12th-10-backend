package com.dnd.reevserver.domain.template.entity;

import com.dnd.reevserver.domain.category.entity.TemplateCategory;
import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template extends BaseEntity {
    @Id
    @Column(name = "template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @Column(name = "template_name", nullable = false, length = 200)
    private String templateName;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic; // true : public, false : custom

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member; // 유저 전용 템플릿의 소유자 (공용 템플릿일 경우 null)

    @Column(name = "preset", nullable = false, columnDefinition = "TEXT")
    private String preset;

    @OneToMany(mappedBy = "template")
    private List<TemplateCategory> templateCategories = new ArrayList<>();

    public void updateTemplateName(String newTemplateName) {
        this.templateName = newTemplateName;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void updateDescription(String newDescription) { this.preset = newDescription; }

    public void clearTemplateCategory(){
        this.templateCategories.clear();
    }

    public void addTemplateCategory(TemplateCategory templateCategory) {
        templateCategories.add(templateCategory);
        templateCategory.updateTemplate(this);
    }
}
