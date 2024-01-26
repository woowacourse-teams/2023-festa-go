package com.festago.festival.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record FestivalV1ListResponse(
    boolean isLastPage,
    List<FestivalV1Response> festivals
) {

    @QueryProjection
    public FestivalV1ListResponse {
    }
}
