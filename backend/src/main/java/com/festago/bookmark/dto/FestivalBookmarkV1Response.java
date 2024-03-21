package com.festago.bookmark.dto;

import com.festago.festival.dto.FestivalV1Response;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record FestivalBookmarkV1Response(
    FestivalV1Response festival,
    LocalDateTime createdAt
) {

    @QueryProjection
    public FestivalBookmarkV1Response {
    }
}
