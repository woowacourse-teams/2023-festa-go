package com.festago.festival.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

//TODO: 필드명 변경으로 변경 불가
public record StageV1Response(
    Long id,
    LocalDateTime startDateTime,
    @JsonRawValue
    String artists
) {

    @QueryProjection
    public StageV1Response {
    }
}
