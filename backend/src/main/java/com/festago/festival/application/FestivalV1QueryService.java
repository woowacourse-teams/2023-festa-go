package com.festago.festival.application;

import com.festago.festival.dto.FestivalV1ListRequest;
import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.repository.FestivalPageable;
import com.festago.festival.repository.FestivalRepository;
import java.time.Clock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
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
        LocalDate now = LocalDate.now(clock);
        return festivalRepository.findBy(request.getFilter(), request.getLocation(), makePageable(request), now);
    }

    private FestivalPageable makePageable(FestivalV1ListRequest request) {
        if (request.getLastStartDate().isPresent() && request.getLastFestivalId().isPresent()) {
            return new FestivalPageable(request.getLastStartDate().get(), request.getLastFestivalId().get(),
                request.getLimit());
        }
        return new FestivalPageable(null, null, request.getLimit());
    }
}
