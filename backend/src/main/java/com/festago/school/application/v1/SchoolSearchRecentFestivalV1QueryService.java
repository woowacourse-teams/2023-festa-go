package com.festago.school.application.v1;

import com.festago.school.dto.v1.SchoolSearchRecentFestivalV1Response;
import java.util.List;
import java.util.Map;

public interface SchoolSearchRecentFestivalV1QueryService {

    Map<Long, SchoolSearchRecentFestivalV1Response> searchRecentFestivals(List<Long> schoolIds);
}
