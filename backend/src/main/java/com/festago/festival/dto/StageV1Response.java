package com.festago.festival.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.festago.artist.infrastructure.JsonArtistsSerializer;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record StageV1Response(
    Long id,
    LocalDateTime startDateTime,
    @JsonRawValue
    @ArraySchema(schema = @Schema(implementation = JsonArtistsSerializer.ArtistQueryModel.class))
    String artists
) {

    @QueryProjection
    public StageV1Response {
    }
}
