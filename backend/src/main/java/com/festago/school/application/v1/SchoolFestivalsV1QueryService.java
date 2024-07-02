package com.festago.school.application.v1;

import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.school.repository.v1.SchoolFestivalsV1QueryDslRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolFestivalsV1QueryService {

    public static final String SCHOOL_FESTIVALS_V1_CACHE_NAME = "schoolFestivalsV1";
    public static final String PAST_SCHOOL_FESTIVALS_V1_CACHE_NAME = "pastSchoolFestivalsV1";

    private final SchoolFestivalsV1QueryDslRepository schoolFestivalsV1QueryDslRepository;
    private final Clock clock;

    @Cacheable(cacheNames = SCHOOL_FESTIVALS_V1_CACHE_NAME, key = "#schoolId")
    public List<SchoolFestivalV1Response> findFestivalsBySchoolId(Long schoolId) {
        LocalDate now = LocalDate.now(clock);
        return schoolFestivalsV1QueryDslRepository.findFestivalsBySchoolId(schoolId, now);
    }

    @Cacheable(cacheNames = PAST_SCHOOL_FESTIVALS_V1_CACHE_NAME, key = "#schoolId")
    public List<SchoolFestivalV1Response> findPastFestivalsBySchoolId(Long schoolId) {
        LocalDate now = LocalDate.now(clock);
        return schoolFestivalsV1QueryDslRepository.findPastFestivalsBySchoolId(schoolId, now);
    }
}
