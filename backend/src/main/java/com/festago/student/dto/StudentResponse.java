package com.festago.student.dto;

import com.festago.school.domain.School;

public record StudentResponse(
    boolean isVerified,
    StudentSchoolResponse school
) {

    private static final StudentResponse NOT_VERIFIED = new StudentResponse(false, null);

    public static StudentResponse verified(School school) {
        return new StudentResponse(
            true,
            StudentSchoolResponse.from(school)
        );
    }

    public static StudentResponse notVerified() {
        return NOT_VERIFIED;
    }
}
