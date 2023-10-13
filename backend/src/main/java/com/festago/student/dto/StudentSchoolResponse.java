package com.festago.student.dto;

import com.festago.school.domain.School;

public record StudentSchoolResponse(
    Long id,
    String schoolName,
    String domain
) {

    public static StudentSchoolResponse from(School school) {
        return new StudentSchoolResponse(
            school.getId(),
            school.getName(),
            school.getDomain()
        );
    }
}
