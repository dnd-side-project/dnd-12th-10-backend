package com.dnd.reevserver.domain.template.repository;

import com.dnd.reevserver.domain.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    @Query("SELECT t FROM Template t " +
            "LEFT JOIN FETCH t.templateCategories tc " +
            "LEFT JOIN FETCH tc.category " +
            "WHERE t.member.userId = :userId AND t.isPublic = false")
    List<Template> findByMemberUserIdAndIsPublicFalse(@Param("userId") String userId);


    @Query("SELECT t FROM Template t " +
            "LEFT JOIN FETCH t.templateCategories tc " +
            "LEFT JOIN FETCH tc.category " +
            "WHERE t.isPublic = true")
    List<Template> findByIsPublicTrueWithCategories();


    Template findByTemplateName(String templateName);

    @Query("SELECT t FROM Template t LEFT JOIN FETCH t.templateCategories tc LEFT JOIN FETCH tc.category WHERE t.templateId = :id")
    Optional<Template> findByIdWithCategories(@Param("id") Long id);

}
