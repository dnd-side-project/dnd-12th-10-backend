package com.dnd.reevserver.domain.category.service;

import com.dnd.reevserver.domain.category.entity.TeamCategory;
import com.dnd.reevserver.domain.category.repository.TeamCategoryRepository;
import com.dnd.reevserver.domain.category.repository.batch.TeamCategoryBatchRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamCategoryService {
    private final TeamCategoryRepository teamCategoryRepository;
    private final TeamCategoryBatchRepository teamCategoryBatchRepository;

    @Transactional
    public void updateTeamCategories(Long groupId, List<TeamCategory> teamCategories) {
        teamCategoryRepository.deleteAllByGroupId(groupId);
        teamCategoryBatchRepository.saveAll(teamCategories);
    }
}
