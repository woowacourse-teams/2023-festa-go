package com.festago.admin.dto.artist;

import com.festago.artist.domain.Artist;

public record AdminArtistV1Response(
    Long id,
    String name,
    String profileImage
) {

    public static AdminArtistV1Response from(Artist artist) {
        return new AdminArtistV1Response(artist.getId(), artist.getName(), artist.getProfileImage());
    }
}
