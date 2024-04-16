package com.festago.school.application.v2;

import com.festago.school.dto.v1.SchoolFestivalV1Response;
import com.festago.school.repository.v2.SchoolFestivalsV2QueryDslRepository;
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
public class SchoolFestivalsV2QueryService {

    public static final String SCHOOL_FESTIVALS_V2_CACHE_NAME = "schoolFestivalsV2";
    public static final String PAST_SCHOOL_FESTIVALS_V2_CACHE_NAME = "pastSchoolFestivalsV2";

    private final SchoolFestivalsV2QueryDslRepository schoolFestivalsV1QueryDslRepository;
    private final Clock clock;

    @Cacheable(cacheNames = SCHOOL_FESTIVALS_V2_CACHE_NAME, key = "#schoolId")
    public List<SchoolFestivalV1Response> findFestivalsBySchoolId(Long schoolId) {
        LocalDate now = LocalDate.now(clock);
        return schoolFestivalsV1QueryDslRepository.findFestivalsBySchoolId(schoolId, now);
    }

    @Cacheable(cacheNames = PAST_SCHOOL_FESTIVALS_V2_CACHE_NAME, key = "#schoolId")
    public List<SchoolFestivalV1Response> findPastFestivalsBySchoolId(Long schoolId) {
        LocalDate now = LocalDate.now(clock);
        return schoolFestivalsV1QueryDslRepository.findPastFestivalsBySchoolId(schoolId, now);
    }
}
