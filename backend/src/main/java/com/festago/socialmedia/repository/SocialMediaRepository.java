package com.festago.socialmedia.repository;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SocialMediaRepository extends Repository<SocialMedia, Long> {

    default SocialMedia getOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.SOCIAL_MEDIA_NOT_FOUND));
    }

    SocialMedia save(SocialMedia socialMedia);

    Optional<SocialMedia> findById(Long id);

    boolean existsByOwnerIdAndOwnerTypeAndMediaType(Long ownerId, OwnerType ownerType, SocialMediaType mediaType);

    void deleteById(Long socialMediaId);
}
