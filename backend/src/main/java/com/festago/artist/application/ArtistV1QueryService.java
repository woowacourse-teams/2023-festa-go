package com.festago.artist.application;

import com.festago.admin.dto.ArtistV1Response;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtistV1QueryService {

    private final ArtistRepository artistRepository;

    public ArtistV1Response findById(Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        return ArtistV1Response.from(artist);
    }

    public List<ArtistV1Response> findAll() {
        return artistRepository.findAll().stream()
            .map(ArtistV1Response::from)
            .toList();
    }
}
