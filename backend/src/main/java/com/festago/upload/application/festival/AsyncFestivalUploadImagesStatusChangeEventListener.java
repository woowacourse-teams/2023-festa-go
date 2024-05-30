package com.festago.upload.application.festival;

import static com.festago.upload.domain.FileOwnerType.FESTIVAL;

import com.festago.festival.domain.Festival;
import com.festago.festival.dto.event.FestivalCreatedEvent;
import com.festago.festival.dto.event.FestivalDeletedEvent;
import com.festago.festival.dto.event.FestivalUpdatedEvent;
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
public class AsyncFestivalUploadImagesStatusChangeEventListener {

    private final UploadFileStatusChangeService uploadFileStatusChangeService;

    @TransactionalEventListener(value = FestivalCreatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeAttachedStatusFestivalImagesEventHandler(FestivalCreatedEvent event) {
        Festival festival = event.festival();
        Long festivalId = festival.getId();
        List<String> imageUris = festival.getImageUrls();
        uploadFileStatusChangeService.changeAttached(festivalId, FESTIVAL, imageUris);
    }

    @TransactionalEventListener(value = FestivalUpdatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeRenewalStatusFestivalImagesEventHandler(FestivalUpdatedEvent event) {
        Festival festival = event.festival();
        Long festivalId = festival.getId();
        List<String> imageUris = festival.getImageUrls();
        uploadFileStatusChangeService.changeRenewal(festivalId, FESTIVAL, imageUris);
    }

    @TransactionalEventListener(value = FestivalDeletedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeAbandonedStatusFestivalImagesEventHandler(FestivalDeletedEvent event) {
        uploadFileStatusChangeService.changeAllAbandoned(event.festivalId(), FESTIVAL);
    }
}
