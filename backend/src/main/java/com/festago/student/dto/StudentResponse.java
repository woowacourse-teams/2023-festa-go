package com.festago.student.dto;

import com.festago.school.domain.School;

public record StudentResponse(
    boolean isVerified,
    StudentSchoolResponse school
) {

    public static StudentResponse verified(School school) {
        return new StudentResponse(
            true,
            StudentSchoolResponse.from(school)
        );
    }

    public static StudentResponse notVerified() {
        return new StudentResponse(
            false,
            null
        );
    }
}
