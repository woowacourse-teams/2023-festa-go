package com.festago.festival.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record FestivalV1Response(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String imageUrl,
    SchoolV1Response school,
    String artists
) {

    @QueryProjection
    public FestivalV1Response {
    }
}
