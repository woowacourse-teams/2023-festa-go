package com.festago.admin.dto;

import com.festago.school.domain.School;

public record AdminSchoolResponse(
    Long id,
    String domain,
    String name) {

    public static AdminSchoolResponse from(School school) {
        return new AdminSchoolResponse(
            school.getId(),
            school.getDomain(),
            school.getName()
        );
    }
}
