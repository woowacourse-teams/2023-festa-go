package com.festago.upload.repository;

import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.domain.UploadStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class MemoryUploadFileRepository implements UploadFileRepository {

    private final HashMap<UUID, UploadFile> memory = new HashMap<>();

    @Override
    public UploadFile save(UploadFile uploadFile) {
        memory.put(uploadFile.getId(), uploadFile);
        return uploadFile;
    }

    @Override
    public Optional<UploadFile> findById(UUID id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public List<UploadFile> findAllByOwnerIdAndOwnerType(Long ownerId, FileOwnerType ownerType) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getOwnerId(), ownerId))
            .filter(it -> Objects.equals(it.getOwnerType(), ownerType))
            .toList();
    }

    @Override
    public List<UploadFile> findByIdIn(Collection<UUID> ids) {
        return memory.values().stream()
            .filter(uploadFile -> ids.contains(uploadFile.getId()))
            .toList();
    }

    @Override
    public List<UploadFile> findByCreatedAtBetweenAndStatus(LocalDateTime startTime, LocalDateTime endTime,
                                                            UploadStatus status) {
        return memory.values().stream()
            .filter(it -> it.getStatus() == status)
            .filter(it -> it.getCreatedAt().isEqual(startTime) || it.getCreatedAt().isAfter(startTime))
            .filter(it -> it.getCreatedAt().isEqual(endTime) || it.getCreatedAt().isBefore(endTime))
            .toList();
    }

    @Override
    public List<UploadFile> findByCreatedAtBeforeAndStatus(LocalDateTime createdAt, UploadStatus status) {
        return memory.values().stream()
            .filter(it -> it.getStatus() == status)
            .filter(it -> it.getCreatedAt().isBefore(createdAt))
            .toList();
    }

    @Override
    public void deleteByIn(List<UploadFile> uploadFiles) {
        for (UploadFile uploadFile : uploadFiles) {
            memory.remove(uploadFile.getId());
        }
    }
}
