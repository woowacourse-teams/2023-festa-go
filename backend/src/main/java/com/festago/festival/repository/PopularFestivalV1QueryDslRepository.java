package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;
import static com.festago.school.domain.QSchool.school;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.domain.Festival;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.QFestivalV1Response;
import com.festago.festival.dto.QSchoolV1Response;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PopularFestivalV1QueryDslRepository extends QueryDslRepositorySupport {

    private static final int POPULAR_FESTIVAL_LIMIT_COUNT = 7;

    public PopularFestivalV1QueryDslRepository() {
        super(Festival.class);
    }

    /**
     * 아직 명확한 추천 축제 기준이 없으므로 생성 시간(식별자) 내림차순으로 반환하도록 함
     */
    public List<FestivalV1Response> findPopularFestivals(LocalDate now) {
        return select(new QFestivalV1Response(
            festival.id,
            festival.name,
            festival.festivalDuration.startDate,
            festival.festivalDuration.endDate,
            festival.posterImageUrl,
            new QSchoolV1Response(
                school.id,
                school.name
            ),
            festivalQueryInfo.artistInfo)
        )
            .from(festival)
            .where(festivalQueryInfo.artistInfo.ne("[]"))
            .innerJoin(school).on(school.id.eq(festival.school.id))
            .innerJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
            .orderBy(festival.id.desc())
            .limit(POPULAR_FESTIVAL_LIMIT_COUNT)
            .fetch();
    }
}
