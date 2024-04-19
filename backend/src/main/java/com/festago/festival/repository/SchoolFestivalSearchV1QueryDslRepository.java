package com.festago.festival.repository;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.querydsl.QueryDslRepositorySupport;
import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.dto.QFestivalSearchV1Response;
import com.festago.school.domain.School;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SchoolFestivalSearchV1QueryDslRepository extends QueryDslRepositorySupport {

    protected SchoolFestivalSearchV1QueryDslRepository() {
        super(School.class);
    }

    public List<FestivalSearchV1Response> executeSearch(String keyword) {
        int keywordLength = keyword.length();
        if (keywordLength == 0) {
            throw new BadRequestException(ErrorCode.INVALID_KEYWORD);
        }

        return select(
            new QFestivalSearchV1Response(
                festival.id,
                festival.name,
                festival.festivalDuration.startDate,
                festival.festivalDuration.endDate,
                festival.thumbnail,
                festivalQueryInfo.artistInfo))
            .from(festival)
            .innerJoin(festivalQueryInfo).on(festival.id.eq(festivalQueryInfo.festivalId))
            .where(festival.name.contains(keyword))
            .fetch();
    }
}
