package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.TemplateCategory;
import com.dnd.reevserver.domain.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TemplateCategoryRepository extends JpaRepository<TemplateCategory, Long> {
    @Modifying
    @Query("DELETE FROM TemplateCategory tc " +
            "WHERE tc.template = :template")
    void deleteAllByTemplate(@Param("template") Template template);

}
