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

    private final FestivalRepository festivalRepository;
    private final Clock clock;

    public Slice<FestivalV1Response> findFestivals(FestivalV1QueryRequest request) {
        return festivalRepository.findBy(makeSearchCondition(request));
    }

    private FestivalSearchCondition makeSearchCondition(FestivalV1QueryRequest request) {
        LocalDate now = LocalDate.now(clock);
        return new FestivalSearchCondition(
            getOrDefaultFilter(request.filter()),
            getOrDefaultRegion(request.location()),
            request.lastStartDate(),
            request.lastFestivalId(),
            PageRequest.ofSize(request.limit()),
            now
        );
    }

    private FestivalFilter getOrDefaultFilter(FestivalFilter filter) {
        if (filter == null) {
            return FestivalFilter.PROGRESS;
        }
        return filter;
    }

    private SchoolRegion getOrDefaultRegion(SchoolRegion region) {
        if (region == null) {
            return SchoolRegion.기타;
        }
        return region;
    }
}

