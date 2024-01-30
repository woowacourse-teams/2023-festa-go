package com.festago.festival.application;

import com.festago.festival.dto.FestivalV1QueryRequest;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.repository.FestivalFilter;
import com.festago.festival.repository.FestivalRepository;
import com.festago.festival.repository.FestivalSearchCondition;
import com.festago.school.domain.SchoolRegion;
import java.time.Clock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalV1QueryService {

    private final FestivalV1QueryDslRepository festivalV1QueryDslRepository;
    private final Clock clock;

    public Slice<FestivalV1Response> findFestivals(Pageable pageable, FestivalV1QueryRequest request) {
        return festivalV1QueryDslRepository.f(new FestivalSearchCondition(
            request.filter(),
            request.location(),
            request.lastStartDate(),
            request.lastFestivalId(),
            pageable,
            LocalDate.now(clock)
        ));
}

