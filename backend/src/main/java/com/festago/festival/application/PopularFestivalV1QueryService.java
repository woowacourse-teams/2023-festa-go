package com.festago.festival.application;

import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.PopularFestivalsV1Response;
import com.festago.festival.repository.PopularFestivalV1QueryDslRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PopularFestivalV1QueryService {

    private final PopularFestivalV1QueryDslRepository popularFestivalRepository;
    private final Clock clock;

    public PopularFestivalsV1Response findPopularFestivals() {
        List<FestivalV1Response> popularFestivals = popularFestivalRepository.findPopularFestivals(LocalDate.now(clock));
        return new PopularFestivalsV1Response(
            "요즘 뜨는 축제",
            popularFestivals
        );
    }
}
