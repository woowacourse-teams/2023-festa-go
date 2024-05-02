package com.festago.upload.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.util.Validator;
import com.festago.upload.domain.FileExtension;
import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.StorageClient;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.dto.FileUploadResult;
import com.festago.upload.repository.UploadFileRepository;
import com.festago.upload.util.FileNameExtensionParser;
import jakarta.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
// 명시적으로 @Transactional 사용하지 않음
public class ImageFileUploadService {

    private static final int MAX_FILE_SIZE = 2_000_000; // 2MB
    private static final Set<FileExtension> ALLOW_IMAGE_EXTENSION = EnumSet.of(FileExtension.JPG, FileExtension.PNG);

    private final StorageClient storageClient;
    private final UploadFileRepository uploadFileRepository;

    public FileUploadResult upload(MultipartFile image, @Nullable Long ownerId, @Nullable FileOwnerType ownerType) {
        validate(image);
        UploadFile uploadImage = storageClient.storage(image);
        if (ownerId != null && ownerType != null) {
            uploadImage.changeAssigned(ownerId, ownerType);
        }

        uploadFileRepository.save(uploadImage);

        return new FileUploadResult(uploadImage.getId(), uploadImage.getUploadUri());
    }

    private void validate(MultipartFile image) {
        validateSize(image.getSize());
        validateExtension(image.getOriginalFilename());
    }

    private void validateSize(long imageSize) {
        Validator.maxValue(imageSize, MAX_FILE_SIZE, "imageSize");
    }

    private void validateExtension(String imageName) {
        Validator.notBlank(imageName, "imageName");
        String extension = FileNameExtensionParser.parse(imageName);
        for (FileExtension allowExtension : ALLOW_IMAGE_EXTENSION) {
            if (allowExtension.match(extension)) {
                return;
            }
        }
        log.info("허용되지 않은 확장자에 대한 이미지 업로드 요청이 있습니다. fileName={}, extension={}", imageName, extension);
        throw new BadRequestException(ErrorCode.NOT_SUPPORT_FILE_EXTENSION);
    }
}
