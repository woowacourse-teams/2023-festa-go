package com.festago.common.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

public abstract class QueryDslRepositorySupport {

    private final Class<?> domainClass;
    private Querydsl querydsl;
    private EntityManager entityManager;
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
        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @PostConstruct
    protected void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    protected <T> JPAQuery<T> select(Expression<T> expr) {
        return queryFactory.select(expr);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
        return queryFactory.selectFrom(from);
    }

    protected <T> Optional<T> fetchOne(
        Function<JPAQueryFactory, JPAQuery<T>> queryFunction
    ) {
        JPAQuery<T> query = queryFunction.apply(queryFactory);
        return Optional.ofNullable(query.fetchOne());
    }

    protected <T> Page<T> applyPagination(Pageable pageable,
                                          Function<JPAQueryFactory, JPAQuery<T>> contentQueryFunction,
                                          Function<JPAQueryFactory, JPAQuery<Long>> countQueryFunction
    ) {
        var contentQuery = contentQueryFunction.apply(queryFactory);
        var content = querydsl.applyPagination(pageable, contentQuery).fetch();
        JPAQuery<Long> countQuery = countQueryFunction.apply(queryFactory);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
