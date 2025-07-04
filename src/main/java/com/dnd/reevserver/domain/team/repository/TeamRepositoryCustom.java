package com.dnd.reevserver.domain.team.repository;

import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.team.dto.request.GroupSearchCondition;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import com.dnd.reevserver.domain.team.entity.Team;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TeamRepositoryCustom {
    List<Team> search(GroupSearchCondition condition);
    List<Team> searchForKeyword(String keyword);
    Slice<Team> searchForKeywordParti(String keyword, Pageable pageable);
}
