package com.festago.artist.dto.command;

import lombok.Builder;

@Builder
public record ArtistUpdateCommand(
    String name,
    String profileImageUrl,
    String backgroundImageUrl
) {

}
