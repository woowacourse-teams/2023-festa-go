package com.festago.artist.dto.command;

public record ArtistCreateCommand(
    String name,
    String profileImageUrl,
    String backgroundImageUrl
) {

}
