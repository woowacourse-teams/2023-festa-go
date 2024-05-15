package com.festago.upload.application;

import com.festago.upload.domain.StorageClient;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.domain.UploadStatus;
import com.festago.upload.repository.UploadFileRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadFileDeleteService {

    private final StorageClient storageClient;
    private final UploadFileRepository uploadFileRepository;
    private final Clock clock;

    public void deleteAbandonedStatusWithPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        List<UploadFile> uploadFiles = uploadFileRepository.findByCreatedAtBetweenAndStatus(startTime, endTime, UploadStatus.ABANDONED);
        deleteUploadFiles(uploadFiles);
    }

    private void deleteUploadFiles(List<UploadFile> uploadFiles) {
        storageClient.delete(uploadFiles);
        uploadFileRepository.deleteByIn(uploadFiles);
    }

    public void deleteOldUploadedStatus() {
        LocalDateTime yesterday = LocalDateTime.now(clock).minusDays(1);
        List<UploadFile> uploadFiles = uploadFileRepository.findByCreatedAtBeforeAndStatus(yesterday, UploadStatus.UPLOADED);
        deleteUploadFiles(uploadFiles);
    }
}
