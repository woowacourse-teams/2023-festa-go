package com.festago.admin.dto.school;

import com.festago.school.domain.SchoolRegion;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record AdminSchoolV1Response(
    Long id,
    String domain,
    String name,
    SchoolRegion region,
    String logoUrl,
    String backgroundImageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    @QueryProjection
    public AdminSchoolV1Response {
        // for QueryProjection
    }
}
