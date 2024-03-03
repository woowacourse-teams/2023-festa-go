package com.festago.festival.application;

import com.festago.festival.dto.ArtistsSearchV1Response;
import com.festago.festival.repository.ArtistFestivalSearchV1QueryDslRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FestivalSearchV1QueryService {

    private final ArtistFestivalSearchV1QueryDslRepository artistFestivalSearchV1QueryDslRepository;

    public Optional<ArtistsSearchV1Response> search(String keyword) {
        return artistFestivalSearchV1QueryDslRepository.executeSearch(keyword);
    }
}
