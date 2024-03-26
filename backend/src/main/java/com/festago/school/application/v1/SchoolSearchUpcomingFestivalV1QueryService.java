package com.festago.school.application.v1;

import com.festago.school.dto.v1.SchoolSearchUpcomingFestivalV1Response;
import java.util.List;
import java.util.Map;

public interface SchoolSearchUpcomingFestivalV1QueryService {

    Map<Long, SchoolSearchUpcomingFestivalV1Response> searchUpcomingFestivals(List<Long> schoolIds);
}
