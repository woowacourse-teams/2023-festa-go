package com.festago.admin.dto.socialmedia;

import com.festago.socialmedia.domain.OwnerType;
import com.festago.socialmedia.domain.SocialMediaType;
import com.querydsl.core.annotations.QueryProjection;

public record AdminSocialMediaV1Response(
    Long id,
    Long ownerId,
    OwnerType ownerType,
    SocialMediaType socialMediaType,
    String name,
    String logoUrl,
    String url
) {

    @QueryProjection
    public AdminSocialMediaV1Response {
    }
}
