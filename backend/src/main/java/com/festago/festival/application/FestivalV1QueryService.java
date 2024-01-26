package com.festago.festival.application;

import com.festago.festival.dto.FestivalV1ListRequest;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.repository.FestivalRepository;
import com.festago.festival.repository.FestivalSearchCondition;
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

    public Slice<FestivalV1Response> findFestivals(FestivalV1ListRequest request) {
        return festivalRepository.findBy(makeSearchCondition(request));
    }

    private FestivalSearchCondition makeSearchCondition(FestivalV1ListRequest request) {
        LocalDate now = LocalDate.now(clock);
        if (request.getLastFestivalId().isPresent() && request.getLastStartDate().isPresent()) {
            return new FestivalSearchCondition(
                request.getFilter(),
                request.getLocation(),
                request.getLastStartDate().get(),
                request.getLastFestivalId().get(),
                PageRequest.ofSize(request.getLimit()),
                now
            );
        }
        return new FestivalSearchCondition(
            request.getFilter(),
            request.getLocation(),
            null,
            null,
            PageRequest.ofSize(request.getLimit()),
            now
        );
    }
}
