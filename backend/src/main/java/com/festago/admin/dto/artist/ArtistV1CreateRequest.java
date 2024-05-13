package com.festago.admin.dto.artist;

import com.festago.artist.dto.command.ArtistCreateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record ArtistV1CreateRequest(
    @NotBlank
    String name,
    @Nullable
    String profileImageUrl,
    @Nullable
    String backgroundImageUrl
) {

    public ArtistCreateCommand toCommand() {
        return new ArtistCreateCommand(
            name,
            profileImageUrl,
            backgroundImageUrl
        );
    }
}
