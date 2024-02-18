package com.festago.school.application;

import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalResponse;
import com.festago.school.repository.v1.SchoolV1QueryDslRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolV1QueryService {

    private final SchoolV1QueryDslRepository schoolV1QueryDslRepository;

    public SchoolDetailV1Response findById(Long schoolId) {
        return schoolV1QueryDslRepository.findById(schoolId);
    }

    public List<SchoolFestivalResponse> findAll(Long schoolId, LocalDate today, Long lastFestivalId,
                                                LocalDate startDate) {
        return schoolV1QueryDslRepository.findCurrentFestivalBySchoolId(schoolId, today, 3, lastFestivalId, startDate);
    }
}
