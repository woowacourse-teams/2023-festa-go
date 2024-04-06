package com.festago.socialmedia.repository;

import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;
import org.springframework.data.repository.Repository;

public interface SocialMediaRepository extends Repository<SocialMedia, Long> {

    SocialMedia save(SocialMedia socialMedia);

    boolean existsByOwnerIdAndOwnerTypeAndMediaType(Long ownerId, OwnerType ownerType, SocialMediaType mediaType);
}
