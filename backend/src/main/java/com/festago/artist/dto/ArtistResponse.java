package com.festago.artist.dto;

import com.festago.artist.domain.Artist;

public record ArtistResponse(
    Long id,
    String name,
    String profileImage
) {

    public static ArtistResponse from(Artist artist) {
        return new ArtistResponse(artist.getId(), artist.getName(), artist.getProfileImage());
    }
}
