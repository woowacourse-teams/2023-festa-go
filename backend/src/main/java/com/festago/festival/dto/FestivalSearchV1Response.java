package com.festago.festival.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record FestivalSearchV1Response(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String posterImageUrl,
    @JsonRawValue String artists
) {

    @QueryProjection
    public FestivalSearchV1Response {

    }
}
