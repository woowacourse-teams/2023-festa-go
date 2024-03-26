package com.festago.artist.dto.command;

public record ArtistUpdateCommand(
    String name,
    String profileImageUrl,
    String backgroundImageUrl
) {

}
