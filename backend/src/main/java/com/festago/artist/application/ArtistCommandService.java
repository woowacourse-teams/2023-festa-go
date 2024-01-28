package com.festago.artist.application;

import com.festago.admin.dto.ArtistCreateRequest;
import com.festago.admin.dto.ArtistUpdateRequest;
import com.festago.artist.domain.Artist;
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
        Artist artist = artistRepository.save(new Artist(request.name(), request.profileImage()));
        return artist.getId();
    }

    public void update(ArtistUpdateRequest request, Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        artist.update(request.name(), request.profileImage());
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }
}
