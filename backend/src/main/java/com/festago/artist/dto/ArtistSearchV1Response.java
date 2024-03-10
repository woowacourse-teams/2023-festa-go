package com.festago.artist.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ArtistSearchV1Response(
    Long id,
    String name,
    String profileImageUrl
) {

    @QueryProjection
    public ArtistSearchV1Response {
    }
}
