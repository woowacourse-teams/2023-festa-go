package com.festago.school.application.v1;

import com.festago.school.dto.v1.SchoolSearchV1Response;
import com.festago.school.dto.v1.SchoolTotalSearchV1Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolTotalSearchV1QueryService {

    private final SchoolSearchV1QueryService schoolSearchV1QueryService;
    private final SchoolUpcomingFestivalStartDateV1QueryService schoolUpcomingFestivalStartDateV1QueryService;

    public List<SchoolTotalSearchV1Response> searchSchools(String keyword) {
        List<SchoolSearchV1Response> schoolSearchResponses = schoolSearchV1QueryService.searchSchools(keyword);
        List<Long> schoolIds = schoolSearchResponses.stream()
            .map(SchoolSearchV1Response::id)
            .toList();
        Map<Long, LocalDate> schoolIdToUpcomingFestivalStartDate = getSchoolIdToUpcomingFestivalStartDate(schoolIds);
        return schoolSearchResponses.stream()
            .map(schoolSearchResponse -> new SchoolTotalSearchV1Response(
                schoolSearchResponse.id(),
                schoolSearchResponse.name(),
                schoolSearchResponse.logoUrl(),
                schoolIdToUpcomingFestivalStartDate.get(schoolSearchResponse.id())
            ))
            .toList();
    }

    private Map<Long, LocalDate> getSchoolIdToUpcomingFestivalStartDate(List<Long> schoolIds) {
        return schoolUpcomingFestivalStartDateV1QueryService.getSchoolIdToUpcomingFestivalStartDate(schoolIds);
    }
}
