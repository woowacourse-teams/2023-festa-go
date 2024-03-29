package com.festago.artist.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record ArtistFestivalDetailV1Response(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String posterImageUrl,
    @JsonRawValue String artists
) {

    @QueryProjection
    public ArtistFestivalDetailV1Response {

    }
}
