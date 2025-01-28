package com.dnd.reevserver.domain.memo.repository;

import com.dnd.reevserver.domain.member.entity.Member;
import com.dnd.reevserver.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findMemosByMember(Member member);
}
