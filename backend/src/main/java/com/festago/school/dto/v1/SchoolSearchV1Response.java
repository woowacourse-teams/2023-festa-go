package com.festago.school.dto.v1;

import com.querydsl.core.annotations.QueryProjection;

public record SchoolSearchV1Response(
    Long id,
    String name,
    String logoUrl
) {

    @QueryProjection
    public SchoolSearchV1Response {
    }
}
