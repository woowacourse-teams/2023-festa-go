package com.festago.socialmedia.dto.command;

import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMedia;
import com.festago.socialmedia.domain.SocialMediaType;

public record SocialMediaCreateCommand(
    Long ownerId,
    OwnerType ownerType,
    SocialMediaType socialMediaType,
    String name,
    String logoUrl,
    String url
) {

    public SocialMedia toEntity() {
        return new SocialMedia(
            ownerId,
            ownerType,
            socialMediaType,
            name,
            logoUrl,
            url
        );
    }
}
