package com.festago.upload.repository;

import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
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
}
