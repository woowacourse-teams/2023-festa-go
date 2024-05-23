package com.festago.common.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryDslHelper {

    private static final int NEXT_PAGE_OFFSET = 1;
    private final JPAQueryFactory queryFactory;

    public <T> JPAQuery<T> select(Expression<T> expr) {
        return queryFactory.select(expr);
    }

    public <T> JPAQuery<T> selectFrom(EntityPath<T> expr) {
        return queryFactory.selectFrom(expr);
    }

    public <T> Optional<T> fetchOne(Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
        JPAQuery<T> query = queryFunction.apply(queryFactory);
        return Optional.ofNullable(query.fetchOne());
    }

    public <T> Page<T> applyPagination(
        Pageable pageable,
        Function<JPAQueryFactory, JPAQuery<T>> contentQueryFunction,
        Function<JPAQueryFactory, JPAQuery<Long>> countQueryFunction
    ) {
        List<T> content = contentQueryFunction.apply(queryFactory).fetch();
        JPAQuery<Long> countQuery = countQueryFunction.apply(queryFactory);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public <T> Slice<T> applySlice(
        Pageable pageable,
        Function<JPAQueryFactory, JPAQuery<T>> contentQueryFunction
    ) {
        List<T> content = contentQueryFunction.apply(queryFactory)
            .limit(pageable.getPageSize() + (long) NEXT_PAGE_OFFSET)
            .fetch();
        if (content.size() > pageable.getPageSize()) {
            removeTemporaryContent(content);
            return new SliceImpl<>(content, pageable, true);
        }
        return new SliceImpl<>(content, pageable, false);
    }

    private <T> void removeTemporaryContent(List<T> content) {
        content.remove(content.size() - NEXT_PAGE_OFFSET);
    }
}
