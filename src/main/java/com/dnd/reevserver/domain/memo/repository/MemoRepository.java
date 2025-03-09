package com.dnd.reevserver.domain.memo.repository;

import com.dnd.reevserver.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("SELECT m FROM Memo m " +
            "JOIN FETCH m.member " +
            "JOIN FETCH m.template " +
            "WHERE m.member.userId = :userId")
    List<Memo> findMemosByMemberUserId(@Param("userId") String userId);
}
