package com.dnd.reevserver.domain.retrospect.repository;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.team.entity.Team;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {

    @Query("select r from Retrospect r "
        + "join fetch r.member "
        + "join fetch r.team "
        + "where r.team.groupId = :groupId")
    List<Retrospect> findAllByTeamId(@Param("groupId") Long groupId);

    @Query("select r from Retrospect r "
        + "join fetch r.team "
        + "where r.member.userId = :userId")
    List<Retrospect> findAllByUserId(@Param("userId") String userId);

    @Query("select count(r) from Retrospect r WHERE r.team.groupId = :groupId")
    long countByGroupId(Long groupId);

    Optional<Retrospect> findFirstByTeam_GroupIdOrderByUpdatedAtDesc(Long groupId);

    @Query("SELECT r FROM Retrospect r WHERE r.retrospectId IN " +
            "(SELECT b.retrospect.retrospectId FROM Bookmark b WHERE b.member.userId = :userId)")
    List<Retrospect> findRetrospectsByUserId(@Param("userId") Long userId);
}
