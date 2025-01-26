package com.dnd.reevserver.domain.member.repository;

import com.dnd.reevserver.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserId(String userId);

    boolean existsByUserId(String kakaoId);
}
