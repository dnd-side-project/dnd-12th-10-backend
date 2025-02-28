package com.dnd.reevserver.domain.category.repository;

import com.dnd.reevserver.domain.category.entity.MemoCategory;
import com.dnd.reevserver.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoCategoryRepository extends JpaRepository<MemoCategory, Long> {
    List<MemoCategory> findByMemo(Memo memo);
}
