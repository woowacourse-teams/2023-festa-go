package com.festago.festival.dto.v1;

import com.festago.artist.domain.Artist;

public record ArtistV1Response(
    Long id,
    String name,
    String imageUrl
) {

    public static ArtistV1Response from(Artist artist) {
        return new ArtistV1Response(artist.getId(), artist.getName(), artist.getProfileImage());
    }
}
