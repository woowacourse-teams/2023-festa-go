package com.festago.festival.application;

import com.festago.festival.repository.RecentSchoolFestivalV1QueryDslRepository;
import com.festago.school.application.v1.SchoolSearchRecentFestivalV1QueryService;
import com.festago.school.dto.v1.SchoolSearchRecentFestivalV1Response;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslSchoolSearchRecentFestivalV1QueryService implements SchoolSearchRecentFestivalV1QueryService {

    private final RecentSchoolFestivalV1QueryDslRepository recentSchoolFestivalV1QueryDslRepository;
    private final Clock clock;

    @Override
    public List<SchoolSearchRecentFestivalV1Response> searchRecentFestivals(List<Long> schoolIds) {
        return recentSchoolFestivalV1QueryDslRepository.findRecentSchoolFestivals(schoolIds, LocalDate.now(clock));
    }
}
