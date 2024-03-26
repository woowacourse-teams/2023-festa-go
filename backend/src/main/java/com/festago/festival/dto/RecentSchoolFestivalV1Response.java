package com.festago.festival.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record RecentSchoolFestivalV1Response(
    Long festivalId,
    Long schoolId,
    LocalDate festivalStartDate,
    LocalDate festivalEndDate
) {

    @QueryProjection
    public RecentSchoolFestivalV1Response {
    }
}
