package com.dnd.reevserver.domain.userTeam.repository;

import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    @Query("SELECT ut FROM UserTeam ut WHERE ut.member.userId = :userId AND ut.team.groupId = :groupId")
    Optional<UserTeam> findByUserIdAndGroupId(@Param("userId") String userId, @Param("groupId") Long groupId);

    @Query("SELECT ut FROM UserTeam ut WHERE ut.member.userId = :userId AND ut.isFavorite = true")
    List<UserTeam> findAllFavoriteGroupsByUserId(@Param("userId") String userId);
}
