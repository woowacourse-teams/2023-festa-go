package com.festago.festival.dto;

import com.festago.school.domain.School;
import com.festago.school.domain.SchoolRegion;
import com.querydsl.core.annotations.QueryProjection;

public record SchoolV1Response(
    Long id,
    String name,
    SchoolRegion region
) {

    @QueryProjection
    public SchoolV1Response {
    }

    public static SchoolV1Response from(School school) {
        return new SchoolV1Response(school.getId(), school.getName(), school.getRegion());
    }
}
