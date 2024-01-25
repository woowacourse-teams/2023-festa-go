package com.festago.artist.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.ArtistV1CreateRequest;
import com.festago.artist.dto.ArtistV1UpdateRequest;
import com.festago.artist.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistV1CommandService {

    private final ArtistRepository artistRepository;

    public Long save(ArtistV1CreateRequest request) {
        return artistRepository.save(new Artist(request.name(), request.profileImage()))
                .getId();
    }

    public void update(ArtistV1UpdateRequest request, Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        artist.update(request.name(), request.profileImage());
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }
}
