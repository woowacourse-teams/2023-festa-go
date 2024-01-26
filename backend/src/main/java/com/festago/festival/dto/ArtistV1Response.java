package com.festago.festival.dto;

import com.festago.artist.domain.Artist;
import com.querydsl.core.annotations.QueryProjection;

public record ArtistV1Response(
    Long id,
    String name,
    String imageUrl
) {

    @QueryProjection
    public ArtistV1Response {
    }

    public static ArtistV1Response from(Artist artist) {
        return new ArtistV1Response(artist.getId(), artist.getName(), artist.getProfileImage());
    }
}
