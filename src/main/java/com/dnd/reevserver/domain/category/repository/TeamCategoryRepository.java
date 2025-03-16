package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.TeamCategory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamCategoryRepository extends JpaRepository<TeamCategory, Long> {
    @Modifying
    @Query("delete from TeamCategory tc "
        + "where tc.team.groupId = :groupId")
    void deleteAllByGroupId(@Param("groupId") Long groupId);
}
