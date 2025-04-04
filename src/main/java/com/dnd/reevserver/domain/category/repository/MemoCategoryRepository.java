package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.MemoCategory;
import com.dnd.reevserver.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoCategoryRepository extends JpaRepository<MemoCategory, Long> {
    @Modifying
    @Query("DELETE FROM MemoCategory mc " +
            "WHERE mc.memo = :memo")
    void deleteAllByMemo(@Param("memo") Memo memo);
}
