package com.festago.festival.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record StageV1Response(
    Long id,
    LocalDateTime startDate,
    @JsonRawValue
    String artists
) {

    @QueryProjection
    public StageV1Response {
    }
}
