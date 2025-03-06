package com.dnd.reevserver.domain.category.repository.batch;

import com.dnd.reevserver.domain.category.entity.MemoCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemoCategoryBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<MemoCategory> mcList){
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.batchUpdate(
                "INSERT INTO memo_category(`category_id`, `memo_id`, `created_at`, `updated_at`) VALUES (?,?,?,?)",
                mcList, // insert할 데이터 리스트
                50, // 1회에 진행할 배치 사이즈
                (ps, mc) -> {
                    ps.setLong(1, mc.getCategory().getCategoryId());
                    ps.setLong(2, mc.getMemo().getMemoId());
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                }

        );
    }
}
