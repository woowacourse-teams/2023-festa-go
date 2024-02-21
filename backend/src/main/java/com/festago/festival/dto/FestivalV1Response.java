package com.festago.festival.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

//TODO: 아티스트 필드명을 변경할려면 JsonRawValue 형식이라 DB에 저장할 필드이름을 다르게 해야함
public record FestivalV1Response(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String posterImageUrl,
    SchoolV1Response school,
    @JsonRawValue String artists) {

    @QueryProjection
    public FestivalV1Response {
    }
}
