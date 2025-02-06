package com.dnd.reevserver.domain.userTeam.repository;

import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    @Query("SELECT ut FROM UserTeam ut WHERE ut.member.userId = :userId AND ut.team.teamId = :teamId")
    Optional<UserTeam> findByUserIdAndGroupId(@Param("userId") String userId, @Param("teamId") Long teamId);
}
