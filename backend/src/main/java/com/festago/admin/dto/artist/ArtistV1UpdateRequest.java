package com.festago.admin.dto.artist;

import com.festago.artist.dto.command.ArtistUpdateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record ArtistV1UpdateRequest(
    @NotBlank
    String name,
    @Nullable
    String profileImageUrl,
    @Nullable
    String backgroundImageUrl
) {

    public ArtistUpdateCommand toCommand() {
        return ArtistUpdateCommand.builder()
            .name(name)
            .profileImageUrl(profileImageUrl)
            .backgroundImageUrl(backgroundImageUrl)
            .build();
    }
}
