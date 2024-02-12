package com.festago.festival.dto;

import com.festago.socialmedia.domain.SocialMediaType;
import com.querydsl.core.annotations.QueryProjection;

public record SocialMediaV1Response(
    SocialMediaType type,
    String name,
    String logoUrl,
    String url
) {

    @QueryProjection
    public SocialMediaV1Response {
    }
}
