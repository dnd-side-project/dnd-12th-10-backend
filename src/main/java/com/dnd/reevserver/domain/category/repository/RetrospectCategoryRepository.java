package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.RetrospectCategory;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RetrospectCategoryRepository extends JpaRepository<RetrospectCategory, Long> {
    @Modifying
    @Query("DELETE FROM RetrospectCategory rc " +
            "WHERE rc.retrospect = :retrospect")
    void deleteAllByRetrospect(@Param("retrospect") Retrospect retrospect);
}
