package com.dnd.reevserver.domain.member.repository;

import com.dnd.reevserver.domain.member.entity.FeatureKeyword;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeatureKeywordRepository extends JpaRepository<FeatureKeyword, Long> {
    @Query("select fk from FeatureKeyword fk where fk.member.userId = :userId")
    List<FeatureKeyword> findAllByUserId(@Param("userId") String userId);
}

