package com.festago.admin.dto.school;

import com.festago.school.domain.SchoolRegion;
import com.querydsl.core.annotations.QueryProjection;

public record AdminSchoolV1Response(
    Long id,
    String domain,
    String name,
    SchoolRegion region
) {

    @QueryProjection
    public AdminSchoolV1Response {
        // for QueryProjection
    }
}
