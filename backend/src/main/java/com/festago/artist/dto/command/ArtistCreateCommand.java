package com.festago.artist.dto.command;

import lombok.Builder;

@Builder
public record ArtistCreateCommand(
    String name,
    String profileImageUrl,
    String backgroundImageUrl
) {

}
