package com.festago.artist.application;

import com.festago.artist.dto.ArtistsSearchV1Response;
import com.festago.artist.repository.ArtistSearchV1QueryDslRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistSearchV1QueryService {

    private final ArtistSearchV1QueryDslRepository artistSearchV1QueryDslRepository;

    public Optional<ArtistsSearchV1Response> search(String keyword) {
        return artistSearchV1QueryDslRepository.executeSearch(keyword);
    }
}
