package com.dnd.reevserver.domain.memo.repository;

import com.dnd.reevserver.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    @Query("SELECT m FROM Memo m " +
            "JOIN FETCH m.member " +
            "LEFT JOIN FETCH m.team "+
            "WHERE m.member.userId = :userId")
    List<Memo> findMemosByMemberUserId(@Param("userId") String userId);

    @Query("SELECT m FROM Memo m " +
            "JOIN FETCH m.member " +
            "JOIN FETCH m.team "+
            "WHERE m.member.userId = :userId " +
            "AND m.team.groupId = :groupId")
    List<Memo> findMemosByMemberUserIdAndGroupId(@Param("userId") String userId, @Param("groupId") Long groupId);

    @Modifying
    @Query("DELETE FROM MemoCategory mc " +
            "WHERE mc.memo = :memo")
    void deleteAllByMemo(@Param("memo") Memo memo);

    @Query(value = "SELECT m.memo_id FROM memo m " +
            "WHERE m.user_id = :userId " +
            "ORDER BY m.created_at ASC " +
            "LIMIT 1", nativeQuery = true)
    Long findOldestMemoIdByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(m) FROM Memo m WHERE m.member.userId = :userId")
    long countByUserId(@Param("userId") String userId);

}
