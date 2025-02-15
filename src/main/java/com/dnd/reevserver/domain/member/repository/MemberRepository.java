package com.dnd.reevserver.domain.member.repository;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.team.entity.Team;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserId(String userId);

    boolean existsByUserId(String userId);

    @Query("select t from Member m " +
            "join m.userGroups ut " +
            "join ut.team t " +
            "where m.userId = :userId ")
    List<Team> findGroupsByUserId(@Param("userId") String userId);
}
