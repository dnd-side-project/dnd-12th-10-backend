package com.dnd.reevserver.domain.template.repository;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    @Query("SELECT t FROM Template t WHERE t.member = :member AND t.isPublic = false")
    List<Template> findByMemberAndIsPublicFalse(@Param("member") Member member);

    @Query("SELECT t FROM Template t WHERE t.isPublic = true")
    List<Template> findByIsPublicTrue();
}
