package com.dnd.reevserver.domain.alert.repository;

import com.dnd.reevserver.domain.alert.entity.Alert;
import com.dnd.reevserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByMemberAndReadFalse(Member member);
    List<Alert> findByMember(Member member);
    long countByMemberAndReadFalse(Member member);
}
