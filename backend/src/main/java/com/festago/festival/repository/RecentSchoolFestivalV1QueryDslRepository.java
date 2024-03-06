package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import com.festago.school.dto.v1.QSchoolSearchRecentFestivalV1Response;
import com.festago.school.dto.v1.SchoolSearchRecentFestivalV1Response;
import com.querydsl.core.types.dsl.Expressions;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RecentSchoolFestivalV1QueryDslRepository extends QueryDslRepositorySupport {

    private static final String ANY_VALUE_TEMPLATE = "any_value({0})";

    public RecentSchoolFestivalV1QueryDslRepository() {
        super(Festival.class);
    }

    public List<SchoolSearchRecentFestivalV1Response> findRecentSchoolFestivals(
        List<Long> schoolIds,
        LocalDate now
    ) {
        return select(
            new QSchoolSearchRecentFestivalV1Response(
                Expressions.numberTemplate(Long.class, ANY_VALUE_TEMPLATE, festival.id),
                Expressions.numberTemplate(Long.class, ANY_VALUE_TEMPLATE, festival.school.id),
                festival.startDate.min(),
                Expressions.dateTimeTemplate(LocalDate.class, ANY_VALUE_TEMPLATE, festival.endDate)
            ))
            .from(festival)
            .where(festival.school.id.in(schoolIds).and(festival.startDate.goe(now)))
            .groupBy(festival.school.id)
            .fetch();
    }
}
