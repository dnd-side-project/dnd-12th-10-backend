package com.dnd.reevserver.domain.group.repository;

import com.dnd.reevserver.domain.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
