package com.festago.festival.dto;

import com.festago.artist.dto.ArtistFestivalDetailV1Response;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record ArtistSearchV1Response(
    Long id,
    String artistName,
    String profileImageUrl,
    List<ArtistFestivalDetailV1Response> festivals
) {

    @QueryProjection
    public ArtistSearchV1Response {

    }
}
