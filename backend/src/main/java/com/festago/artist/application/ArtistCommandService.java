package com.festago.artist.application;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.command.ArtistCreateCommand;
import com.festago.artist.dto.command.ArtistUpdateCommand;
import com.festago.artist.dto.event.ArtistCreatedEvent;
import com.festago.artist.dto.event.ArtistDeletedEvent;
import com.festago.artist.dto.event.ArtistUpdatedEvent;
import com.festago.artist.repository.ArtistRepository;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistCommandService {

    private final ArtistRepository artistRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long save(ArtistCreateCommand command) {
        validateSave(command);
        Artist artist = artistRepository.save(
            new Artist(command.name(), command.profileImageUrl(), command.backgroundImageUrl())
        );
        eventPublisher.publishEvent(new ArtistCreatedEvent(artist));
        return artist.getId();
    }

    private void validateSave(ArtistCreateCommand command) {
        if (artistRepository.existsByName(command.name())) {
            throw new BadRequestException(ErrorCode.DUPLICATE_ARTIST_NAME);
        }
    }

    public void update(ArtistUpdateCommand command, Long artistId) {
        Artist artist = artistRepository.getOrThrow(artistId);
        artist.update(command.name(), command.profileImageUrl(), command.backgroundImageUrl());
        eventPublisher.publishEvent(new ArtistUpdatedEvent(artist));
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
        eventPublisher.publishEvent(new ArtistDeletedEvent(artistId));
    }
}
