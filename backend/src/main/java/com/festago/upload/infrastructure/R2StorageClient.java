package com.festago.upload.infrastructure;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import com.festago.upload.domain.FileExtension;
import com.festago.upload.domain.StorageClient;
import com.festago.upload.domain.UploadFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Error;

@Slf4j
@Component
public class R2StorageClient implements StorageClient {

    private final S3Client s3Client;
    private final String bucket;
    private final URI uri;
    private final Clock clock;

    public R2StorageClient(
        @Value("${festago.r2.access-key}") String accessKey,
        @Value("${festago.r2.secret-key}") String secretKey,
        @Value("${festago.r2.endpoint}") String endpoint,
        @Value("${festago.r2.bucket}") String bucket,
        @Value("${festago.r2.url}") String uri,
        Clock clock
    ) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
            .endpointOverride(URI.create(endpoint))
            .region(Region.of("auto"))
            .build();
        this.bucket = bucket;
        this.uri = URI.create(uri);
        this.clock = clock;
    }

    @Override
    public UploadFile storage(MultipartFile file) {
        UploadFile uploadFile = createUploadFile(file);
        upload(file, uploadFile);
        return uploadFile;
    }

    private UploadFile createUploadFile(MultipartFile file) {
        return new UploadFile(file.getSize(), uri, FileExtension.from(file.getContentType()), LocalDateTime.now(clock));
    }

    private void upload(MultipartFile file, UploadFile uploadFile) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .key(uploadFile.getName())
            .bucket(bucket)
            .build();
        try (InputStream inputStream = file.getInputStream()) {
            long fileSize = uploadFile.getSize();
            String mimeType = uploadFile.getMimeType().toString();
            RequestBody requestBody = RequestBody.fromContentProvider(() -> inputStream, fileSize, mimeType);
            UUID uploadFileId = uploadFile.getId();
            log.info("파일 업로드 시작. id = {}, uploadUri={}, size={}", uploadFileId, uploadFile.getUploadUri(), fileSize);
            s3Client.putObject(objectRequest, requestBody);
            log.info("파일 업로드 완료. id = {}", uploadFileId);
        } catch (IOException e) {
            log.warn("파일 업로드 중 문제가 발생했습니다. id = {}", uploadFile.getId());
            throw new InternalServerException(ErrorCode.FILE_UPLOAD_ERROR, e);
        }
    }

    @Override
    public void delete(List<UploadFile> uploadFiles) {
        if (uploadFiles.isEmpty()) {
            log.info("삭제하려는 파일이 없습니다.");
            return;
        }
        int fileSize = uploadFiles.size();
        UUID firstFileId = uploadFiles.get(0).getId();
        DeleteObjectsRequest deleteObjectsRequest = getDeleteObjectsRequest(uploadFiles);

        log.info("{}개 파일 삭제 시작. 첫 번째 파일 식별자={}", fileSize, firstFileId);
        DeleteObjectsResponse response = s3Client.deleteObjects(deleteObjectsRequest);
        log.info("{}개 파일 삭제 완료. 첫 번째 파일 식별자={}", fileSize, firstFileId);

        if (response.hasErrors()) {
            List<S3Error> errors = response.errors();
            log.warn("{}개 파일 삭제 중 에러가 발생했습니다. 첫 번째 파일 식별자={}, 에러 개수={}", fileSize, firstFileId, errors.size());
            errors.forEach(error -> log.info("파일 삭제 중 에러가 발생했습니다. key={}, message={}", error.key(), error.message()));
        }
    }

    private DeleteObjectsRequest getDeleteObjectsRequest(List<UploadFile> uploadFiles) {
        List<ObjectIdentifier> objectIdentifiers = uploadFiles.stream()
            .map(UploadFile::getName)
            .map(name -> ObjectIdentifier.builder().key(name).build())
            .toList();
        return DeleteObjectsRequest.builder()
            .bucket(bucket)
            .delete(builder -> builder.objects(objectIdentifiers).build())
            .build();
    }
}
