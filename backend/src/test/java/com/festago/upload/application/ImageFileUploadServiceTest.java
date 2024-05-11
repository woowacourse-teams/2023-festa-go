package com.festago.upload.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.domain.UploadStatus;
import com.festago.upload.dto.FileUploadResult;
import com.festago.upload.infrastructure.FakeStorageClient;
import com.festago.upload.repository.MemoryUploadFileRepository;
import com.festago.upload.repository.UploadFileRepository;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageFileUploadServiceTest {

    ImageFileUploadService imageFileUploadService;

    UploadFileRepository uploadFileRepository;

    @BeforeEach
    void setUp() {
        uploadFileRepository = new MemoryUploadFileRepository();
        imageFileUploadService = new ImageFileUploadService(
            new FakeStorageClient(),
            uploadFileRepository
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"image.png", "image.jpg", "image.jpeg"})
    void 이미지를_업로드할때_JPG_PNG_확장자이면_성공한다(String filename) {
        // given
        MultipartFile multipartFile = new MockMultipartFile("image", filename, "image/png",
            "data".getBytes(StandardCharsets.UTF_8));

        // when
        FileUploadResult result = imageFileUploadService.upload(multipartFile, null, null);

        // then
        UUID uploadFileId = result.uploadFileId();
        assertThat(uploadFileRepository.findById(uploadFileId)).isPresent();
    }

    @ParameterizedTest
    @ValueSource(strings = {"image", "image.exe", "image.txt", "image.gif"})
    void 이미지를_업로드할떄_JPG_PNG_확장자가_아니면_실패한다(String filename) {
        // given
        MultipartFile multipartFile = new MockMultipartFile("image", filename, "image/png",
            "data".getBytes(StandardCharsets.UTF_8));

        // when & then
        assertThatThrownBy(() -> imageFileUploadService.upload(multipartFile, null, null))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ErrorCode.NOT_SUPPORT_FILE_EXTENSION.getMessage());
    }

    @Test
    void 이미지를_업로드할때__ownerId_ownerType이_null이면_UPLOADED_상태로_영속된다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("image", "image.png", "image/png",
            "data".getBytes(StandardCharsets.UTF_8));

        // when
        FileUploadResult result = imageFileUploadService.upload(multipartFile, null, null);

        // then
        UUID uploadFileId = result.uploadFileId();
        UploadFile uploadFile = uploadFileRepository.findById(uploadFileId).get();
        assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.UPLOADED);
    }

    @Test
    void 이미지를_업로드할때_ownerId_ownerType이_null이_아니면_ASSGINED_상태로_영속된다() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("image", "image.png", "image/png",
            "data".getBytes(StandardCharsets.UTF_8));

        // when
        FileUploadResult result = imageFileUploadService.upload(multipartFile, 1L, FileOwnerType.SCHOOL);

        // then
        UUID uploadFileId = result.uploadFileId();
        UploadFile uploadFile = uploadFileRepository.findById(uploadFileId).get();
        assertThat(uploadFile.getStatus()).isEqualTo(UploadStatus.ASSIGNED);
    }
}
