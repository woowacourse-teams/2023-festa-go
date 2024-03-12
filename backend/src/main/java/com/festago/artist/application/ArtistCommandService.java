package com.festago.artist.application;

import com.festago.admin.dto.artist.ArtistCreateRequest;
import com.festago.admin.dto.artist.ArtistUpdateRequest;
import com.festago.artist.domain.Artist;
import com.festago.artist.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO Artist Command DTO 사용하여 admin 패키지 의존 제거
@Service
@Transactional
@RequiredArgsConstructor
public class ArtistCommandService {

    private final ArtistRepository artistRepository;

    public Long save(ArtistCreateRequest request) {
        Artist artist = artistRepository.save(
            new Artist(request.name(), request.profileImage(), request.backgroundImageUrl()));
        return artist.getId();
    }

    public void update(ArtistUpdateRequest request, Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        artist.update(request.name(), request.profileImage(), request.backgroundImageUrl());
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }
}
