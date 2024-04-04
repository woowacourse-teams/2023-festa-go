package com.festago.admin.dto.artist;

import com.querydsl.core.annotations.QueryProjection;

public record AdminArtistV1Response(
    Long id,
    String name,
    String profileImageUrl,
    String backgroundImageUrl
) {

    @QueryProjection
    public AdminArtistV1Response {
    }
}
