package com.dnd.reevserver.domain.category.entity;

import com.dnd.reevserver.domain.template.entity.Template;
import com.dnd.reevserver.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public void updateTemplate(Template template) {
        this.template = template;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public TemplateCategory(Template template, Category category) {
        this.template = template;
        this.category = category;
    }
}
