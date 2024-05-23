package com.festago.upload.application.school;

import static com.festago.upload.domain.FileOwnerType.SCHOOL;

import com.festago.school.domain.School;
import com.festago.school.dto.event.SchoolCreatedEvent;
import com.festago.school.dto.event.SchoolDeletedEvent;
import com.festago.school.dto.event.SchoolUpdatedEvent;
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
public class AsyncSchoolUploadImagesStatusChangeEventListener {

    private final UploadFileStatusChangeService uploadFileStatusChangeService;

    @TransactionalEventListener(value = SchoolCreatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeAttachedStatusSchoolImagesEventHandler(SchoolCreatedEvent event) {
        School school = event.school();
        Long schoolId = school.getId();
        List<String> imageUris = List.of(school.getBackgroundUrl(), school.getLogoUrl());
        uploadFileStatusChangeService.changeAttached(schoolId, SCHOOL, imageUris);
    }

    @TransactionalEventListener(value = SchoolUpdatedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeRenewalStatusSchoolImagesEventHandler(SchoolUpdatedEvent event) {
        School school = event.school();
        Long schoolId = school.getId();
        List<String> imageUris = List.of(school.getBackgroundUrl(), school.getLogoUrl());
        uploadFileStatusChangeService.changeRenewal(schoolId, SCHOOL, imageUris);
    }

    @TransactionalEventListener(value = SchoolDeletedEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeAbandonedStatusSchoolImagesEventHandler(SchoolDeletedEvent event) {
        uploadFileStatusChangeService.changeAllAbandoned(event.schoolId(), SCHOOL);
    }
}
