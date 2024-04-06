package com.festago.socialmedia.repository;

import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

// TODO #841 이슈 해결되면 반영할 것
public class MemorySocialMediaRepository implements SocialMediaRepository {

    private final Map<Long, SocialMedia> memory = new HashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @Override
    @SneakyThrows
    public SocialMedia save(SocialMedia socialMedia) {
        Field idField = socialMedia.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(socialMedia, autoIncrement.incrementAndGet());
        memory.put(socialMedia.getId(), socialMedia);
        return socialMedia;
    }

    @Override
    public Optional<SocialMedia> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

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

    @Override
    public void deleteById(Long socialMediaId) {
        memory.remove(socialMediaId);
    }
}
