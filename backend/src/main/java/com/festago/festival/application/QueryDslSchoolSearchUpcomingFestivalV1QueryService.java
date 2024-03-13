package com.festago.festival.application;

import static java.util.stream.Collectors.toUnmodifiableMap;

import com.festago.festival.repository.RecentSchoolFestivalV1QueryDslRepository;
import com.festago.school.application.v1.SchoolSearchUpcomingFestivalV1QueryService;
import com.festago.school.dto.v1.SchoolSearchUpcomingFestivalV1Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslSchoolSearchUpcomingFestivalV1QueryService implements SchoolSearchUpcomingFestivalV1QueryService {

    private final RecentSchoolFestivalV1QueryDslRepository recentSchoolFestivalV1QueryDslRepository;
    private final Clock clock;

    @Override
    public Map<Long, SchoolSearchUpcomingFestivalV1Response> searchUpcomingFestivals(List<Long> schoolIds) {
        return recentSchoolFestivalV1QueryDslRepository.findRecentSchoolFestivals(schoolIds, LocalDate.now(clock))
            .stream()
            .collect(toUnmodifiableMap(SchoolSearchUpcomingFestivalV1Response::schoolId, Function.identity()));
    }
}
