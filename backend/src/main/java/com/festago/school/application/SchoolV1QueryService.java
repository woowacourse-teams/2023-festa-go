package com.festago.school.application;

import com.festago.school.dto.v1.SchoolDetailV1Response;
import com.festago.school.dto.v1.SchoolFestivalV1Response;
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

    public SchoolDetailV1Response findDetailById(Long schoolId) {
        return schoolV1QueryDslRepository.findDetailById(schoolId);
    }

    public List<SchoolFestivalV1Response> findFestivalsBySchoolId(Long schoolId, LocalDate today, Long lastFestivalId,
                                                                  LocalDate lastStartDate, Boolean isPast, int size) {
        return schoolV1QueryDslRepository.findFestivalsBySchoolId(schoolId, today, size, lastFestivalId, lastStartDate, isPast);
    }
}
