package com.festago.common.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryDslHelper {

    private final JPAQueryFactory queryFactory;

    public <T> JPAQuery<T> select(Expression<T> expr) {
        return queryFactory.select(expr);
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
}
