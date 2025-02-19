package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.TemplateCategory;
import com.dnd.reevserver.domain.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateCategoryRepository extends JpaRepository<TemplateCategory, Long> {
    void deleteAllByTemplate(Template template);
}
