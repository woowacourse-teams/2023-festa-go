package com.festago.artist.dto;

import com.festago.socialmedia.domain.SocialMediaType;
import com.querydsl.core.annotations.QueryProjection;

public record ArtistMediaV1Response(
    SocialMediaType type,
    String name,
    String logoUrl,
    String url
) {

    @QueryProjection
    public ArtistMediaV1Response {

    }
}
