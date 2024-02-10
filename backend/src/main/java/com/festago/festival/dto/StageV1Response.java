package com.festago.festival.dto;

import java.time.LocalDate;
import java.util.List;

public record StageV1Response(
    Long id,
    LocalDate startDate,
    List<ArtistV1Response> artists
) {

}
