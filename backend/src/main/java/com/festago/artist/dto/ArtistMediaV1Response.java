package com.festago.artist.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ArtistMediaV1Response(
    String type,
    String name,
    String logoUrl,
    String url
) {

    @QueryProjection
    public ArtistMediaV1Response {

    }
}
