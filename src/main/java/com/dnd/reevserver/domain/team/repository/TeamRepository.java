package com.dnd.reevserver.domain.team.repository;

import com.dnd.reevserver.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
