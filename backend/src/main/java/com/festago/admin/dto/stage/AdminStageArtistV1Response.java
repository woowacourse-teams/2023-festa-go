package com.festago.admin.dto.stage;

import com.querydsl.core.annotations.QueryProjection;

public record AdminStageArtistV1Response(
    Long id,
    String name
) {

    @QueryProjection
    public AdminStageArtistV1Response {
    }
}
