package com.festago.festival.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;

public record ArtistsSearchV1Response(
    List<ArtistSearchV1Response> results
) {

    @QueryProjection
    public ArtistsSearchV1Response {
    }
}
