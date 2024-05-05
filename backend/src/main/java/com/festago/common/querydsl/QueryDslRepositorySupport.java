package com.festago.common.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

public abstract class QueryDslRepositorySupport {

    private static final int NEXT_PAGE_OFFSET = 1;
    private final Class<?> domainClass;
    private Querydsl querydsl;
    private JPAQueryFactory queryFactory;

    protected QueryDslRepositorySupport(Class<?> domainClass) {
        this.domainClass = domainClass;
    }

    @Autowired
    protected void setQueryFactory(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        JpaEntityInformation<?, ?> entityInformation =
            JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<?> path = resolver.createPath(entityInformation.getJavaType());
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }

    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return queryFactory.select(expr);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> expr) {
        return queryFactory.selectFrom(expr);
    }

    protected <T> Optional<T> fetchOne(
        Function<JPAQueryFactory, JPAQuery<T>> queryFunction
    ) {
        JPAQuery<T> query = queryFunction.apply(queryFactory);
        return Optional.ofNullable(query.fetchOne());
    }

    protected <T> Page<T> applyPagination(
        Pageable pageable,
        Function<JPAQueryFactory, JPAQuery<T>> contentQueryFunction,
        Function<JPAQueryFactory, JPAQuery<Long>> countQueryFunction
    ) {
        JPAQuery<T> contentQuery = contentQueryFunction.apply(queryFactory);
        List<T> content = querydsl.applyPagination(pageable, contentQuery).fetch();
        JPAQuery<Long> countQuery = countQueryFunction.apply(queryFactory);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    protected <T> Slice<T> applySlice(
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
