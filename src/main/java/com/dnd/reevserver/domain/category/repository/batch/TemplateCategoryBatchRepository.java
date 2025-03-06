package com.dnd.reevserver.domain.category.repository.batch;

import com.dnd.reevserver.domain.category.entity.TemplateCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TemplateCategoryBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<TemplateCategory> tcList){
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.batchUpdate(
                "INSERT INTO template_category(`category_id`, `template_id`, `created_at`, `updated_at`) VALUES (?,?,?,?)",
                tcList, // insert할 데이터 리스트
                50, // 1회에 진행할 배치 사이즈
                (ps, tc) -> {
                    ps.setLong(1, tc.getCategory().getCategoryId());
                    ps.setLong(2, tc.getTemplate().getTemplateId());
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                }

        );
    }
}
