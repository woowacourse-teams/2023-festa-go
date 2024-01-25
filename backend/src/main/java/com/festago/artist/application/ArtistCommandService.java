package com.festago.artist.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistCreateRequest;
import com.festago.artist.dto.ArtistUpdateRequest;
import com.festago.artist.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistCommandService {

    private final ArtistRepository artistRepository;

    public Long save(ArtistCreateRequest request) {
        return artistRepository.save(new Artist(request.name(), request.profileImage()))
                .getId();
    }

    public void update(ArtistUpdateRequest request, Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        artist.update(request.name(), request.profileImage());
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }
}
