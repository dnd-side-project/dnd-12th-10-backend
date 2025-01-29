package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.TeamCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamCategoryRepository extends JpaRepository<TeamCategory, Long> {
}
