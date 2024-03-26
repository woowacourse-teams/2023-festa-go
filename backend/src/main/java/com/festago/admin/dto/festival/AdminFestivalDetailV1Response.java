package com.festago.admin.dto.festival;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminFestivalDetailV1Response(
    Long id,
    String name,
    Long schoolId,
    String schoolName,
    LocalDate startDate,
    LocalDate endDate,
    String posterImageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    @QueryProjection
    public AdminFestivalDetailV1Response {
    }
}
