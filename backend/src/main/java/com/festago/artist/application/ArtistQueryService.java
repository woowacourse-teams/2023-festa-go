package com.festago.artist.application;

import com.festago.admin.dto.ArtistResponse;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistQueryService {

    private final ArtistRepository artistRepository;

    public ArtistResponse findById(Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        return ArtistResponse.from(artist);
    }

    public List<ArtistResponse> findAll() {
        return artistRepository.findAll().stream()
                .map(ArtistResponse::from)
                .toList();
    }
}
