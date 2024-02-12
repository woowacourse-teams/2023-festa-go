package com.festago.festival.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.util.Collection;

public record FestivalDetailV1Response(
    Long id,
    String name,
    SchoolV1Response school,
    LocalDate startDate,
    LocalDate endDate,
    String imageUrl,
    Collection<SocialMediaV1Response> socialMedias,
    Collection<StageV1Response> stages
) {

    @QueryProjection
    public FestivalDetailV1Response {
    }
}
