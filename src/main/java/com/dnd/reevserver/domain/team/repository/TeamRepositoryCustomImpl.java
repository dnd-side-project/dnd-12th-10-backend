package com.dnd.reevserver.domain.team.repository;

import static org.springframework.util.StringUtils.hasText;
import static com.dnd.reevserver.domain.team.entity.QTeam.team;
import static com.dnd.reevserver.domain.category.entity.QTeamCategory.teamCategory;

import com.dnd.reevserver.domain.category.entity.QTeamCategory;
import com.dnd.reevserver.domain.team.dto.request.GroupSearchCondition;
import com.dnd.reevserver.domain.team.entity.Team;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Team> search(GroupSearchCondition condition){
        return queryFactory
            .select(team)
            .from(team)
            .leftJoin(team.teamCategories, teamCategory).fetchJoin()
            .leftJoin(teamCategory.category).fetchJoin()
            .where(
                containTitle(condition.title()),
                categoryIn(condition.categories())
            )
            .fetch();

    }

    private BooleanExpression containTitle(String title){
        if(!hasText(title)){
            return null;
        }
        return team.groupName.containsIgnoreCase(title) ;

    }

    private BooleanExpression categoryIn(List<String> categories) {
        if(categories == null || categories.isEmpty()) {
            return null;
        }
        QTeamCategory tc = new QTeamCategory("tc");
        return team.groupId.in(
            JPAExpressions
                .select(tc.team.groupId)
                .from(tc)
                .where(tc.category.categoryName.in(categories))
        );

    }
}