package com.festago.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistUpdateRequest(
    @NotBlank
    String name,
    @NotBlank
    String profileImage,
    @NotBlank
    String backgroundImageUrl
) {

}
