package com.festago.upload.application.artist;

import static com.festago.upload.domain.FileOwnerType.ARTIST;

import com.festago.artist.domain.Artist;
import com.festago.artist.dto.event.ArtistCreatedEvent;
import com.festago.artist.dto.event.ArtistDeletedEvent;
import com.festago.artist.dto.event.ArtistUpdatedEvent;
import com.festago.upload.application.UploadFileStatusChangeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Async
@Component
@Transactional
@RequiredArgsConstructor
public class AsyncArtistUploadImagesStatusChangeEventListener {

    private final UploadFileStatusChangeService uploadFileStatusChangeService;

    @TransactionalEventListener(value = ArtistCreatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeAttachedStatusArtistImagesEventHandler(ArtistCreatedEvent event) {
        Artist artist = event.artist();
        Long artistId = artist.getId();
        List<String> imageUris = List.of(artist.getProfileImage(), artist.getBackgroundImageUrl());
        uploadFileStatusChangeService.changeAttached(artistId, ARTIST, imageUris);
    }

    @TransactionalEventListener(value = ArtistUpdatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeRenewalStatusArtistImagesEventHandler(ArtistUpdatedEvent event) {
        Artist artist = event.artist();
        Long artistId = artist.getId();
        List<String> imageUris = List.of(artist.getProfileImage(), artist.getBackgroundImageUrl());
        uploadFileStatusChangeService.changeRenewal(artistId, ARTIST, imageUris);
    }

    @TransactionalEventListener(value = ArtistDeletedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeAbandonedStatusArtistImagesEventHandler(ArtistDeletedEvent event) {
        uploadFileStatusChangeService.changeAllAbandoned(event.artistId(), ARTIST);
    }
}
