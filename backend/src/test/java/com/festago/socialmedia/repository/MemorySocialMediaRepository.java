package com.festago.socialmedia.repository;

import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import com.festago.support.AbstractMemoryRepository;
import java.util.Objects;

public class MemorySocialMediaRepository extends AbstractMemoryRepository<SocialMedia> implements
    SocialMediaRepository {

    @Override
    public boolean existsByOwnerIdAndOwnerTypeAndMediaType(
        Long ownerId,
        OwnerType ownerType,
        SocialMediaType mediaType
    ) {
        return memory.values().stream()
            .filter(it -> Objects.equals(it.getOwnerId(), ownerId))
            .filter(it -> it.getOwnerType() == ownerType)
            .anyMatch(it -> it.getMediaType() == mediaType);
    }
}
