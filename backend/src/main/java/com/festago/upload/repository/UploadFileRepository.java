package com.festago.upload.repository;

import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.domain.UploadStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UploadFileRepository extends Repository<UploadFile, UUID> {

    UploadFile save(UploadFile uploadFile);

    Optional<UploadFile> findById(UUID id);

    List<UploadFile> findAllByOwnerIdAndOwnerType(Long ownerId, FileOwnerType ownerType);

    List<UploadFile> findByIdIn(Collection<UUID> ids);

    List<UploadFile> findByCreatedAtBetweenAndStatus(LocalDateTime startTime, LocalDateTime endTime,
                                                     UploadStatus status);

    List<UploadFile> findByCreatedAtBeforeAndStatus(LocalDateTime createdAt, UploadStatus status);

    @Modifying
    @Query("delete from UploadFile uf where uf in :uploadFiles")
    void deleteByIn(@Param("uploadFiles") List<UploadFile> uploadFiles);
}
