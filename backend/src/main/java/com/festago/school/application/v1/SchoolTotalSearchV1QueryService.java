package com.festago.school.application.v1;

import com.festago.school.dto.v1.SchoolSearchV1Response;
import com.festago.school.dto.v1.SchoolTotalSearchV1Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolTotalSearchV1QueryService {

    private final SchoolSearchV1QueryService schoolSearchV1QueryService;
    private final SchoolSearchUpcomingFestivalV1QueryService schoolSearchUpcomingFestivalV1QueryService;

    public List<SchoolTotalSearchV1Response> searchSchools(String keyword) {
        var schoolSearchResponses = schoolSearchV1QueryService.searchSchools(keyword);
        List<Long> schoolIds = schoolSearchResponses.stream()
            .map(SchoolSearchV1Response::id)
            .toList();
        var schoolIdToUpcomingFestivalResponse = schoolSearchUpcomingFestivalV1QueryService.searchUpcomingFestivals(
            schoolIds);
        return schoolSearchResponses.stream()
            .map(it -> SchoolTotalSearchV1Response.of(it, schoolIdToUpcomingFestivalResponse.get(it.id())))
            .toList();
    }
}
