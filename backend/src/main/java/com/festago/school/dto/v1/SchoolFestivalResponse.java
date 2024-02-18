package com.festago.school.dto.v1;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record SchoolFestivalResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String imageUrl,
    @JsonRawValue String artists
) {

    @QueryProjection
    public SchoolFestivalResponse {
    }
}
