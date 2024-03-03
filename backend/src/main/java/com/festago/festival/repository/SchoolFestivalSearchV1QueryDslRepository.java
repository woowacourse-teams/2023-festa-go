package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;

import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.dto.QFestivalSearchV1Response;
import com.festago.school.domain.School;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SchoolFestivalSearchV1QueryDslRepository extends QueryDslRepositorySupport {

    protected SchoolFestivalSearchV1QueryDslRepository() {
        super(School.class);
    }

    public List<FestivalSearchV1Response> executeSearch(String keyword) {
        List<FestivalSearchV1Response> response = select(
            new QFestivalSearchV1Response(
                festival.id,
                festival.name,
                festival.startDate,
                festival.endDate,
                festival.thumbnail,
                festivalQueryInfo.artistInfo))
            .from(festival)
            .innerJoin(festivalQueryInfo).on(festival.id.eq(festivalQueryInfo.festivalId))
            .where(festival.name.contains(keyword))
            .fetch();

        if (response.size() > 5) {
            log.warn("{} 키워드로 검색시 5개를 초과한 검색 결과를 반환하였습니다", keyword);
        }

        return response;
    }
}
