package com.festago.school.dto.v1;

import com.querydsl.core.annotations.QueryProjection;

public record SchoolFestivalArtistResponse(
    Long id,
    String name,
    String imageUrl
) {

    @QueryProjection
    public SchoolFestivalArtistResponse {
    }
}
