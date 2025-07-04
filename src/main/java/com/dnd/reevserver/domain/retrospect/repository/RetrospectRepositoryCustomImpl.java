package com.dnd.reevserver.domain.retrospect.repository;

import static org.springframework.util.StringUtils.hasText;
import static com.dnd.reevserver.domain.retrospect.entity.QRetrospect.retrospect;

import com.dnd.reevserver.domain.retrospect.entity.QRetrospect;
import com.dnd.reevserver.domain.retrospect.entity.Retrospect;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class RetrospectRepositoryCustomImpl implements RetrospectRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Retrospect> searchForKeyword(String keyword) {
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression titleCondition = containTitle(keyword);
        if(titleCondition != null) {
            builder.or(titleCondition);
        }

        BooleanExpression contentCondition = containContent(keyword);
        if(contentCondition != null) {
            builder.or(contentCondition);
        }

        return queryFactory
            .select(retrospect)
            .from(retrospect)
            .where(builder)
            .fetch();
    }

    @Override
    public Slice<Retrospect> searchForKeywordParti(String keyword, Pageable pageable){
        BooleanBuilder builder = new BooleanBuilder()
            .or(containTitle(keyword))
            .or(containContent(keyword));

        List<Retrospect> fetched = queryFactory
            .select(retrospect)
            .from(retrospect)
            .where(builder)
            .orderBy(retrospect.retrospectId.desc())
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
    private BooleanExpression containTitle(String keyword){
        if(!hasText(keyword)){
            return null;
        }
        return retrospect.title.containsIgnoreCase(keyword);
    }

    //내용에 포함
    private BooleanExpression containContent(String keyword){
        if(!hasText(keyword)){
            return null;
        }
        return retrospect.content.containsIgnoreCase(keyword);
    }

}
