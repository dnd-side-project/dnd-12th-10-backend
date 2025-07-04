package com.dnd.reevserver.domain.team.repository;

import static com.dnd.reevserver.domain.retrospect.entity.QRetrospect.retrospect;
import static org.springframework.util.StringUtils.hasText;
import static com.dnd.reevserver.domain.team.entity.QTeam.team;
import static com.dnd.reevserver.domain.category.entity.QTeamCategory.teamCategory;

import com.dnd.reevserver.domain.category.entity.QTeamCategory;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.dnd.reevserver.domain.team.dto.request.GroupSearchCondition;
import com.dnd.reevserver.domain.team.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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


    @Override
    public List<Team> searchForKeyword(String keyword){
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression titleCondition = containTitle(keyword);
        if(titleCondition != null) {
            builder.or(titleCondition);
        }

        BooleanExpression introductionCondition = containIntroduction(keyword);
        if (introductionCondition != null) {
            builder.or(introductionCondition);
        }

        return queryFactory
            .select(team)
            .from(team)
            .where(builder)
            .fetch();
    }

    @Override
    public Slice<Team> searchForKeywordParti(String keyword, Pageable pageable){
        BooleanBuilder builder = new BooleanBuilder()
            .or(containTitle(keyword))
            .or(containIntroduction(keyword));

        List<Team> fetched = queryFactory
            .select(team)
            .from(team)
            .where(builder)
            .orderBy(team.groupId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        boolean hasNext = false;
        if (fetched.size() > pageable.getPageSize()) {
            fetched.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(fetched, pageable, hasNext);
    }

    //제목에 포함
    private BooleanExpression containTitle(String title){
        if(!hasText(title)){
            return null;
        }
        return team.groupName.containsIgnoreCase(title) ;

    }

    //카테고리 포함
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


    //한줄소개에 있는지
    private BooleanExpression containIntroduction(String keyword){
        if(!hasText(keyword)){
            return null;
        }
        return team.introduction.containsIgnoreCase(keyword);
    }
}