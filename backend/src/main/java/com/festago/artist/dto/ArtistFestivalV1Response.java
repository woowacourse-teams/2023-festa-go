package com.festago.artist.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.festago.artist.infrastructure.JsonArtistsSerializer;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ArtistFestivalV1Response(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String posterImageUrl,
    @JsonRawValue
    @ArraySchema(schema = @Schema(implementation = JsonArtistsSerializer.ArtistQueryModel.class))
    String artists
) {

    @QueryProjection
    public ArtistFestivalV1Response {

    }
}
