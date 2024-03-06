package com.festago.school.application.v1;

import static java.util.stream.Collectors.toUnmodifiableMap;

import com.festago.school.dto.v1.SchoolSearchRecentFestivalV1Response;
import com.festago.school.dto.v1.SchoolSearchV1Response;
import com.festago.school.dto.v1.SchoolTotalSearchV1Response;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchoolTotalSearchV1QueryService {

    private final SchoolSearchV1QueryService schoolSearchV1QueryService;
    private final SchoolSearchRecentFestivalV1QueryService schoolSearchRecentFestivalV1QueryService;

    public List<SchoolTotalSearchV1Response> searchSchools(String keyword) {
        var schoolSearchResponses = schoolSearchV1QueryService.searchSchools(keyword);
        List<Long> schoolIds = schoolSearchResponses.stream()
            .map(SchoolSearchV1Response::id)
            .toList();
        var schoolIdToRecentFestivalResponse = getSchoolIdToRecentFestivalResponse(schoolIds);
        return schoolSearchResponses.stream()
            .map(it -> new SchoolTotalSearchV1Response(
                it.id(),
                it.name(),
                it.logoUrl(),
                schoolIdToRecentFestivalResponse.get(it.id())
            ))
            .toList();
    }

    private Map<Long, SchoolSearchRecentFestivalV1Response> getSchoolIdToRecentFestivalResponse(
        List<Long> schoolIds
    ) {
        return schoolSearchRecentFestivalV1QueryService.searchRecentFestivals(schoolIds)
            .stream()
            .collect(toUnmodifiableMap(SchoolSearchRecentFestivalV1Response::schoolId, Function.identity()));
    }
}
