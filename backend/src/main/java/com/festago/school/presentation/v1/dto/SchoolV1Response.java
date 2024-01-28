package com.festago.school.presentation.v1.dto;

import com.festago.school.domain.SchoolRegion;
import com.querydsl.core.annotations.QueryProjection;

public record SchoolV1Response(
    Long id,
    String domain,
    String name,
    SchoolRegion region
) {

    @QueryProjection
    public SchoolV1Response {
        // for QueryProjection
    }
}
