package com.dnd.reevserver.domain.team.repository;

import com.dnd.reevserver.domain.team.dto.request.GroupSearchCondition;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import com.dnd.reevserver.domain.team.entity.Team;
import java.util.List;

public interface TeamRepositoryCustom {
    List<Team> search(GroupSearchCondition condition);
    List<Team> searchForKeyword(String keyword);
}
