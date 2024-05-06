package com.festago.admin.dto.artist;

import com.festago.artist.dto.command.ArtistCreateCommand;
import jakarta.validation.constraints.NotBlank;

public record ArtistV1CreateRequest(
    @NotBlank
    String name,
    @NotBlank
    String profileImageUrl,
    @NotBlank
    String backgroundImageUrl
) {

    public ArtistCreateCommand toCommand() {
        return ArtistCreateCommand.builder()
            .name(name)
            .profileImageUrl(profileImageUrl)
            .backgroundImageUrl(backgroundImageUrl)
            .build();
    }
}
