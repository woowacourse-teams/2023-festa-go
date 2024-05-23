package com.festago.school.application.v1;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SchoolUpcomingFestivalStartDateV1QueryService {

    Map<Long, LocalDate> getSchoolIdToUpcomingFestivalStartDate(List<Long> schoolIds);
}
