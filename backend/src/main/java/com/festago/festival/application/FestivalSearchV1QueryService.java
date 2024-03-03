package com.festago.festival.application;

import com.festago.festival.dto.FestivalSearchV1Response;
import com.festago.festival.repository.ArtistFestivalSearchV1QueryDslRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalSearchV1QueryService {

    private final ArtistFestivalSearchV1QueryDslRepository artistFestivalSearchV1QueryDslRepository;

    public List<FestivalSearchV1Response> search(String keyword) {
        return artistFestivalSearchV1QueryDslRepository.executeSearch(keyword);
    }
}
