package com.dnd.reevserver.domain.category.repository.batch;

import com.dnd.reevserver.domain.category.entity.RetrospectCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RetrospectCategoryBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<RetrospectCategory> retrospectCategoryList){
        LocalDateTime now = LocalDateTime.now();
        String sql = "insert into retrospect_category (retrospect_id, category_id, created_at, updated_at) values (?,?,?,?)";
        jdbcTemplate.batchUpdate(sql,
                retrospectCategoryList,
                50,
                (ps, rc) -> {
                    ps.setLong(1, rc.getRetrospect().getRetrospectId());
                    ps.setLong(2, rc.getCategory().getCategoryId());
                    ps.setTimestamp(3, Timestamp.valueOf(now));
                    ps.setTimestamp(4, Timestamp.valueOf(now));
                }
            );
    }
}
