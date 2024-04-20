package com.festago.school.repository.v1;

import static com.festago.festival.domain.QFestival.festival;
import static com.festago.festival.domain.QFestivalQueryInfo.festivalQueryInfo;

import com.festago.common.querydsl.QueryDslHelper;
import com.festago.school.dto.v1.QSchoolFestivalV1Response;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SchoolFestivalsV1QueryDslRepository {

    private final QueryDslHelper queryDslHelper;

    public List<SchoolFestivalV1Response> findFestivalsBySchoolId(
        Long schoolId,
        LocalDate today
    ) {
        return queryDslHelper.select(
                new QSchoolFestivalV1Response(
                    festival.id,
                    festival.name,
                    festival.festivalDuration.startDate,
                    festival.festivalDuration.endDate,
                    festival.posterImageUrl,
                    festivalQueryInfo.artistInfo
                )
            )
            .from(festival)
            .leftJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
            .where(festival.school.id.eq(schoolId).and(festival.festivalDuration.endDate.goe(today)))
            .stream()
            .sorted(Comparator.comparing(SchoolFestivalV1Response::startDate))
            .toList();
    }

    public List<SchoolFestivalV1Response> findPastFestivalsBySchoolId(
        Long schoolId,
        LocalDate today
    ) {
        return queryDslHelper.select(
                new QSchoolFestivalV1Response(
                    festival.id,
                    festival.name,
                    festival.festivalDuration.startDate,
                    festival.festivalDuration.endDate,
                    festival.posterImageUrl,
                    festivalQueryInfo.artistInfo
                )
            )
            .from(festival)
            .leftJoin(festivalQueryInfo).on(festivalQueryInfo.festivalId.eq(festival.id))
            .where(festival.school.id.eq(schoolId).and(festival.festivalDuration.endDate.lt(today)))
            .stream()
            .sorted(Comparator.comparing(SchoolFestivalV1Response::endDate).reversed())
            .toList();
    }
}
