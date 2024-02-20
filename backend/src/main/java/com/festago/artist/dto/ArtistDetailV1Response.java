package com.festago.artist.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record ArtistDetailV1Response(
    Long id,
    String artistName,
    String logoUrl,
    String backgroundUrl,
    List<ArtistMediaV1Response> socialMedias
) {

    @QueryProjection
    public ArtistDetailV1Response {

    }
}
