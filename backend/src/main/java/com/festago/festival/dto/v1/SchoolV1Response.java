package com.festago.festival.dto.v1;

import com.festago.school.domain.School;

public record SchoolV1Response(
    Long id,
    String name
) {

    public static SchoolV1Response from(School school) {
        return new SchoolV1Response(school.getId(), school.getName());
    }
}
