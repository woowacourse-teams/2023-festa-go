package com.festago.artist.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.artist.dto.command.ArtistUpdateCommand;
import com.festago.artist.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistCommandService {

    private final ArtistRepository artistRepository;

    public Long save(ArtistCreateCommand command) {
        Artist artist = artistRepository.save(
            new Artist(command.name(), command.profileImageUrl(), command.backgroundImageUrl())
        );
        return artist.getId();
    }

    public void update(ArtistUpdateCommand command, Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        artist.update(command.name(), command.profileImageUrl(), command.backgroundImageUrl());
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }
}
