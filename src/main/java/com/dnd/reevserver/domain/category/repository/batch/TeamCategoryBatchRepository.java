package com.dnd.reevserver.domain.category.repository.batch;

import com.dnd.reevserver.domain.category.entity.TeamCategory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TeamCategoryBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<TeamCategory> teamCategoryList) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "insert into team_category (group_id, category_id, created_at, updated_at) values (?,?,?,?)";
        jdbcTemplate.batchUpdate(sql,
            teamCategoryList,
            50,
            (ps, tc) -> {
                ps.setLong(1, tc.getTeam().getGroupId());
                ps.setLong(2, tc.getCategory().getCategoryId());
                ps.setTimestamp(3, Timestamp.valueOf(now));
                ps.setTimestamp(4, Timestamp.valueOf(now));
            }
        );
    }
}
