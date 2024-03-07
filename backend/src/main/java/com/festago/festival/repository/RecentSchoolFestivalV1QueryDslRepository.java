package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import com.festago.school.dto.v1.QSchoolSearchRecentFestivalV1Response;
import com.festago.school.dto.v1.SchoolSearchRecentFestivalV1Response;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RecentSchoolFestivalV1QueryDslRepository extends QueryDslRepositorySupport {

    public RecentSchoolFestivalV1QueryDslRepository() {
        super(Festival.class);
    }

    public List<SchoolSearchRecentFestivalV1Response> findRecentSchoolFestivals(
        List<Long> schoolIds,
        LocalDate now
    ) {
        return select(
            new QSchoolSearchRecentFestivalV1Response(
                festival.school.id,
                festival.startDate.min()
            ))
            .from(festival)
            .where(festival.school.id.in(schoolIds).and(festival.endDate.goe(now)))
            .groupBy(festival.school.id)
            .fetch();
    }
}
