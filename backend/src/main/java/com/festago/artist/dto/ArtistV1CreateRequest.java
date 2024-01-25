package com.festago.artist.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistV1CreateRequest(
    @NotBlank(message = "아티스트 이름은 비어있을 수 없습니다.")
    String name,
    @NotBlank(message = "아티스트 이미지는 비어있을 수 없습니다.")
    String profileImage
) {

}
