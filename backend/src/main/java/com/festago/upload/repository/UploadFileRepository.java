package com.festago.upload.repository;

import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.Repository;

public interface UploadFileRepository extends Repository<UploadFile, UUID> {

    UploadFile save(UploadFile uploadFile);

    Optional<UploadFile> findById(UUID id);

    List<UploadFile> findAllByOwnerIdAndOwnerType(Long ownerId, FileOwnerType ownerType);

    List<UploadFile> findByIdIn(Collection<UUID> ids);
}
