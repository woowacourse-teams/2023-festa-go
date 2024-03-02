package com.festago.artist.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ArtistSearchResponse(
    Long id,
    String name,
    String profileImageUrl
) {

    @QueryProjection
    public ArtistSearchResponse {
    }
}
