package com.festago.school.dto.v1;

import com.festago.socialmedia.domain.SocialMediaType;
import com.querydsl.core.annotations.QueryProjection;

public record SchoolSocialMediaV1Response(
        SocialMediaType type,
        String name,
        String logoUrl,
        String url
) {

    @QueryProjection
    public SchoolSocialMediaV1Response {
    }
}
