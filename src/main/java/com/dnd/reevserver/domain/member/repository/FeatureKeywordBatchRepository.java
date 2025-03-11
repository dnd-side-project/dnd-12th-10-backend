package com.dnd.reevserver.domain.member.repository;

import com.dnd.reevserver.domain.category.entity.MemoCategory;
import com.dnd.reevserver.domain.member.entity.FeatureKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeatureKeywordBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<FeatureKeyword> keywords){
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.batchUpdate(
                "INSERT INTO feature_keyword(`keyword_name`, `user_id`, `created_at`, `updated_at`) VALUES (?,?,?,?)",
                keywords, // insert할 데이터 리스트
                50, // 1회에 진행할 배치 사이즈
                (ps, kw) -> {
                    ps.setString(1, kw.getKeywordName());
                    ps.setString(2, kw.getMember().getUserId());
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                }

        );
    }
}
