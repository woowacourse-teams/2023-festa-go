package com.festago.artist.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record ArtistDetailV1Response(
    Long id,
    String name,
    String profileImageUrl,
    String backgroundImageUrl,
    List<ArtistMediaV1Response> socialMedias
) {

    @QueryProjection
    public ArtistDetailV1Response {

    }
}
