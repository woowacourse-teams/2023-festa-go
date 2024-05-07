package com.festago.artist.dto.event;

import com.festago.artist.domain.Artist;

public record ArtistUpdatedEvent(
    Artist artist
) {

}
