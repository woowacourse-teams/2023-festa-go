package com.festago.student.dto;

import com.festago.student.domain.Student;

public record StudentResponse(
    boolean isVerified,
    StudentSchoolResponse school
) {

    private static final StudentResponse NOT_VERIFIED = new StudentResponse(false, null);

    public static StudentResponse verified(Student student) {
        return new StudentResponse(
            true,
            StudentSchoolResponse.from(student.getSchool())
        );
    }

    public static StudentResponse notVerified() {
        return NOT_VERIFIED;
    }
}
