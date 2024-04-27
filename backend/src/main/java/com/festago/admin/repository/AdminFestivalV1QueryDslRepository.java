package com.festago.admin.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.school.domain.QSchool.school;
import static com.festago.stage.domain.QStage.stage;

import com.festago.admin.dto.festival.AdminFestivalV1Response;
import com.festago.admin.dto.festival.QAdminFestivalV1Response;
import com.festago.common.querydsl.OrderSpecifierUtils;
import com.festago.common.querydsl.QueryDslHelper;
import com.festago.common.querydsl.SearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class AdminFestivalV1QueryDslRepository {

    private final QueryDslHelper queryDslHelper;

    public Page<AdminFestivalV1Response> findAll(SearchCondition searchCondition) {
        Pageable pageable = searchCondition.pageable();
        String searchFilter = searchCondition.searchFilter();
        String searchKeyword = searchCondition.searchKeyword();
        return queryDslHelper.applyPagination(pageable,
            queryFactory -> queryFactory.select(
                    new QAdminFestivalV1Response(
                        festival.id,
                        festival.name,
                        school.name,
                        festival.festivalDuration.startDate,
                        festival.festivalDuration.endDate,
                        stage.count()
                    ))
                .from(festival)
                .innerJoin(school).on(school.id.eq(festival.school.id))
                .leftJoin(stage).on(stage.festival.id.eq(festival.id))
                .where(applySearchFilter(searchFilter, searchKeyword))
                .groupBy(festival.id)
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()),
            queryFactory -> queryFactory.select(festival.count())
                .from(festival)
                .where(applySearchFilter(searchFilter, searchKeyword)));
    }

    private BooleanExpression applySearchFilter(String searchFilter, String searchKeyword) {
        return switch (searchFilter) {
            case "id" -> eqId(searchKeyword);
            case "name" -> containsName(searchKeyword);
            case "schoolName" -> containsSchoolName(searchKeyword);
            default -> null;
        };
    }

    private BooleanExpression eqId(String searchKeyword) {
        if (StringUtils.hasText(searchKeyword)) {
            return festival.id.stringValue().eq(searchKeyword);
        }
        return null;
    }

    private BooleanExpression containsName(String searchKeyword) {
        if (StringUtils.hasText(searchKeyword)) {
            return festival.name.contains(searchKeyword);
        }
        return null;
    }

    private BooleanExpression containsSchoolName(String searchKeyword) {
        if (StringUtils.hasText(searchKeyword)) {
            return school.name.contains(searchKeyword);
        }
        return null;
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort sort) {
        return sort.stream()
            .findFirst()
            .map(it -> switch (it.getProperty()) {
                case "id" -> OrderSpecifierUtils.of(it.getDirection(), festival.id);
                case "name" -> OrderSpecifierUtils.of(it.getDirection(), festival.name);
                case "schoolName" -> OrderSpecifierUtils.of(it.getDirection(), school.name);
                case "startDate" -> OrderSpecifierUtils.of(it.getDirection(), festival.festivalDuration.startDate);
                case "endDate" -> OrderSpecifierUtils.of(it.getDirection(), festival.festivalDuration.endDate);
                default -> OrderSpecifierUtils.NULL;
            })
            .orElse(OrderSpecifierUtils.NULL);
    }
}
