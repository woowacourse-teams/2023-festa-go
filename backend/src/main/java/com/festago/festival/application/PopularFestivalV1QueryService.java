package com.festago.festival.application;

import com.festago.festival.dto.FestivalV1Response;
import com.festago.festival.dto.PopularFestivalsV1Response;
import com.festago.festival.repository.FestivalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PopularFestivalV1QueryService {

    private final FestivalRepository festivalRepository;

    public PopularFestivalsV1Response findPopularFestivals() {
        List<FestivalV1Response> popularFestivals = festivalRepository.findPopularFestival();
        return new PopularFestivalsV1Response(
            "요즘 뜨는 축제",
            popularFestivals
        );
    }

}
