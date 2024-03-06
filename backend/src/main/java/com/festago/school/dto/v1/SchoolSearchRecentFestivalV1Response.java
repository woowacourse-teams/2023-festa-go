package com.festago.school.dto.v1;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record SchoolSearchRecentFestivalV1Response(
    Long id,
    Long schoolId,
    LocalDate startDate,
    LocalDate endDate
) {

    @QueryProjection
    public SchoolSearchRecentFestivalV1Response {
    }
}
