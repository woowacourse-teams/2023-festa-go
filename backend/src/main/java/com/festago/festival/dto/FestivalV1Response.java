package com.festago.festival.dto;

import java.time.LocalDate;
import java.util.List;

public record FestivalV1Response(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String imageUris,
    SchoolV1Response school,
    List<ArtistV1Response> artists
) {

}
