package com.festago.student.domain.validator.school;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.school.domain.validator.SchoolDeleteValidator;
import com.festago.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentSchoolDeleteValidator implements SchoolDeleteValidator {

    private final StudentRepository studentRepository;

    @Override
    public void validate(Long schoolId) {
        if (studentRepository.existsBySchoolId(schoolId)) {
            throw new BadRequestException(ErrorCode.SCHOOL_DELETE_CONSTRAINT_EXISTS_STUDENT);
        }
    }
}
