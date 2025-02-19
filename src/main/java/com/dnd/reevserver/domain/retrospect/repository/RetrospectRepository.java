package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
    @Query("select r from Retrospect r where r.team.groupId = :groupId ")
    List<Retrospect> findAllByTeamId(@Param("groupId") Long groupId);

    @Query("select count(r) from Retrospect r WHERE r.team.groupId = :groupId")
    long countByGroupId(Long groupId);
}
