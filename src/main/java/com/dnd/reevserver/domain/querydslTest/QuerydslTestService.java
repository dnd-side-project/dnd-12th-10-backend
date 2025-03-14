package com.dnd.reevserver.domain.querydslTest;

import static com.dnd.reevserver.domain.team.entity.QTeam.team;

import com.dnd.reevserver.domain.retrospect.repository.RetrospectRepository;
import com.dnd.reevserver.domain.team.dto.response.TeamResponseDto;
import com.dnd.reevserver.domain.team.entity.QTeam;
import com.dnd.reevserver.domain.team.entity.Team;
import com.dnd.reevserver.domain.team.repository.TeamRepository;
import com.dnd.reevserver.global.util.TimeStringUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuerydslTestService {

    private final TeamRepository teamRepository;
    private final TimeStringUtil timeStringUtil;
    private final RetrospectRepository retrospectRepository;

    @PersistenceContext
    private final EntityManager em;

    public TeamResponseDto getQTeam() {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Team findTeam = queryFactory
            .select(team)
            .from(team)
            .limit(1)
            .fetchOne();

        TeamResponseDto responseDto = TeamResponseDto.builder()
            .groupId(findTeam.getGroupId())
            .groupName(findTeam.getGroupName())
            .description(findTeam.getDescription())
            .introduction(findTeam.getIntroduction())
            .userCount(findTeam.getUserTeams().size())
            .recentActString(timeStringUtil.getTimeString(findTeam.getRecentAct()))
            .categoryNames(
                findTeam.getTeamCategories().stream()
                    .map(teamCategory -> teamCategory.getCategory().getCategoryName())
                    .toList()
            )
            .retrospectCount(retrospectRepository.countByGroupId(findTeam.getGroupId()))
            .build();
        return responseDto;
    }

}
