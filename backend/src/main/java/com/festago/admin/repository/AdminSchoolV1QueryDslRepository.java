package com.festago.admin.repository;

import static com.festago.school.domain.QSchool.school;

import com.festago.admin.dto.school.AdminSchoolV1Response;
import com.festago.admin.dto.school.QAdminSchoolV1Response;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.common.querydsl.SearchCondition;
import com.festago.school.domain.School;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class AdminSchoolV1QueryDslRepository extends QueryDslRepositorySupport {

    protected AdminSchoolV1QueryDslRepository() {
        super(School.class);
    }

    public Page<AdminSchoolV1Response> findAll(SearchCondition searchCondition) {
        Pageable pageable = searchCondition.pageable();
        String searchFilter = searchCondition.searchFilter();
        String searchKeyword = searchCondition.searchKeyword();
        return applyPagination(pageable,
            queryFactory -> queryFactory.select(
                    new QAdminSchoolV1Response(
                        school.id,
                        school.domain,
                        school.name,
                        school.region,
                        school.logoUrl,
                        school.backgroundUrl,
                        school.createdAt,
                        school.updatedAt
                    ))
                .from(school)
                .where(containSearchFilter(searchFilter, searchKeyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()),
            queryFactory -> queryFactory.select(school.count())
                .where(containSearchFilter(searchFilter, searchKeyword))
                .from(school));
    }

    private BooleanExpression containSearchFilter(String searchFilter, String searchKeyword) {
        return switch (searchFilter) {
            case "id" -> eqId(searchKeyword);
            case "domain" -> containsDomain(searchKeyword);
            case "name" -> containsName(searchKeyword);
            case "region" -> eqRegion(searchKeyword);
            default -> null;
        };
    }

    private BooleanExpression eqId(String id) {
        if (StringUtils.hasText(id)) {
            return school.id.stringValue().eq(id);
        }
        return null;
    }

    private BooleanExpression containsDomain(String domain) {
        if (StringUtils.hasText(domain)) {
            return school.domain.contains(domain);
        }
        return null;
    }

    private BooleanExpression containsName(String name) {
        if (StringUtils.hasText(name)) {
            return school.name.contains(name);
        }
        return null;
    }

    private BooleanExpression eqRegion(String region) {
        if (StringUtils.hasText(region)) {
            return school.region.stringValue().eq(region);
        }
        return null;
    }

    public Optional<AdminSchoolV1Response> findById(Long schoolId) {
        return fetchOne(queryFactory -> queryFactory.select(
                new QAdminSchoolV1Response(
                    school.id,
                    school.domain,
                    school.name,
                    school.region,
                    school.logoUrl,
                    school.backgroundUrl,
                    school.createdAt,
                    school.updatedAt
                ))
            .from(school)
            .where(school.id.eq(schoolId)));
    }
}
