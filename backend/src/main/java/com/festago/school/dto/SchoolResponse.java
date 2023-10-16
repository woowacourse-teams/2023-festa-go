package com.festago.school.dto;

import com.festago.school.domain.School;

public record SchoolResponse(
    Long id,
    String domain,
    String name) {

    public static SchoolResponse from(School school) {
        return new SchoolResponse(
            school.getId(),
            school.getDomain(),
            school.getName()
        );
    }
}
