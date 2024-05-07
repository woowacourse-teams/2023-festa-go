package com.festago.admin.presentation.v1;

import com.festago.admin.dto.upload.AdminImageUploadV1Response;
import com.festago.upload.application.ImageFileUploadService;
import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.dto.FileUploadResult;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/api/v1/upload/images")
@RequiredArgsConstructor
@Hidden
public class AdminUploadV1Controller {

    private final ImageFileUploadService imageFileUploadService;

    @PostMapping
    public ResponseEntity<AdminImageUploadV1Response> uploadImage(
        @RequestPart MultipartFile image,
        @RequestParam(required = false) Long ownerId,
        @RequestParam(required = false) FileOwnerType ownerType
    ) {
        FileUploadResult result = imageFileUploadService.upload(image, ownerId, ownerType);
        return ResponseEntity.ok()
            .body(new AdminImageUploadV1Response(result.uploadUri()));
    }
}
