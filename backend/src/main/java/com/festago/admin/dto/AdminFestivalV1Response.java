package com.festago.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record AdminFestivalV1Response(
    Long id,
    String name,
    String schoolName,
    LocalDate startDate,
    LocalDate endDate,
    long stageCount
) {

    @QueryProjection
    public AdminFestivalV1Response {
    }
}
