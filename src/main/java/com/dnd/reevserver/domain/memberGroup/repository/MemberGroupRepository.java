package com.dnd.reevserver.domain.memberGroup.repository;

import com.dnd.reevserver.domain.memberGroup.entity.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
}
