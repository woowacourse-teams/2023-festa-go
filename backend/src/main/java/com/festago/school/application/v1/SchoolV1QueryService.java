package com.festago.school.application.v1;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.school.repository.v1.SchoolFestivalV1SearchCondition;
import com.festago.school.repository.v1.SchoolV1QueryDslRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolV1QueryService {

    private final SchoolV1QueryDslRepository schoolV1QueryDslRepository;

    public SchoolDetailV1Response findDetailById(Long schoolId) {
        return schoolV1QueryDslRepository.findDetailById(schoolId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.SCHOOL_NOT_FOUND));
    }

    public Slice<SchoolFestivalV1Response> findFestivalsBySchoolId(
        Long schoolId,
        LocalDate today,
        SchoolFestivalV1SearchCondition searchCondition
    ) {
        return schoolV1QueryDslRepository.findFestivalsBySchoolId(schoolId, today, searchCondition);
    }
}
