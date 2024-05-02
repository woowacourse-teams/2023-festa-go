package com.festago.support.fixture;

import com.festago.upload.domain.FileExtension;
import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
import java.net.URI;
import java.time.LocalDateTime;

public class UploadFileFixture {

    private long size = 0;
    private URI location = URI.create("https://festago.com");
    private FileExtension extension = FileExtension.PNG;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long ownerId = 1L;
    private FileOwnerType ownerType = FileOwnerType.FESTIVAL;

    private UploadFileFixture() {
    }

    public UploadFileFixture size(long size) {
        this.size = size;
        return this;
    }

    public UploadFileFixture location(URI location) {
        this.location = location;
        return this;
    }

    public UploadFileFixture extension(FileExtension extension) {
        this.extension = extension;
        return this;
    }

    public UploadFileFixture createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public UploadFileFixture ownerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public UploadFileFixture ownerType(FileOwnerType ownerType) {
        this.ownerType = ownerType;
        return this;
    }

    public static UploadFileFixture builder() {
        return new UploadFileFixture();
    }

    public UploadFile build() {
        return new UploadFile(
            size,
            location,
            extension,
            createdAt
        );
    }

    public UploadFile buildAssigned() {
        UploadFile uploadFile = build();
        uploadFile.changeAssigned(ownerId, ownerType);
        return uploadFile;
    }

    public UploadFile buildAttached() {
        UploadFile uploadFile = build();
        uploadFile.changeAttached(ownerId, ownerType);
        return uploadFile;
    }

    public UploadFile buildAbandoned() {
        UploadFile uploadFile = build();
        uploadFile.changeAbandoned();
        return uploadFile;
    }
}
