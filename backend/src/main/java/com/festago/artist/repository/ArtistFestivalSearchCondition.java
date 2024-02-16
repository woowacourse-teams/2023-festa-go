package com.festago.artist.repository;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public record ArtistFestivalSearchCondition(
    Long artistId,
    Boolean isPast,
    Long lastFestivalId,
    LocalDate lastStartDate,
    Pageable pageable,
    LocalDate currentTime
) {

}
