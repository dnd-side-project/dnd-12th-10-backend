package com.dnd.reevserver.domain.userTeam.repository;

import com.dnd.reevserver.domain.userTeam.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
}
