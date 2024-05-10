package com.festago.admin.presentation.v1;

import com.festago.admin.dto.upload.AdminDeleteAbandonedPeriodUploadFileV1Request;
import com.festago.upload.application.UploadFileDeleteService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/upload/delete")
@RequiredArgsConstructor
@Hidden
public class AdminUploadFileDeleteV1Controller {

    private final UploadFileDeleteService uploadFileDeleteService;

    @DeleteMapping("/abandoned-period")
    public ResponseEntity<Void> deleteAbandonedWithPeriod(
        @RequestBody @Valid AdminDeleteAbandonedPeriodUploadFileV1Request request
    ) {
        uploadFileDeleteService.deleteAbandonedStatusWithPeriod(request.startTime(), request.endTime());
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("/old-uploaded")
    public ResponseEntity<Void> deleteOldUploaded() {
        uploadFileDeleteService.deleteOldUploadedStatus();
        return ResponseEntity.ok()
            .build();
    }
}
