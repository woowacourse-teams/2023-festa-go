package com.festago.school.dto.v1;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record SchoolSearchRecentFestivalV1Response(
    Long schoolId,
    LocalDate startDate
) {

    @QueryProjection
    public SchoolSearchRecentFestivalV1Response {
    }
}
