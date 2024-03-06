package com.festago.school.application.v1;

import com.festago.school.dto.v1.SchoolSearchRecentFestivalV1Response;
import java.util.List;

public interface SchoolSearchRecentFestivalV1QueryService {

    List<SchoolSearchRecentFestivalV1Response> searchRecentFestivals(List<Long> schoolIds);
}
