package com.festago.school.dto.v1;

import com.festago.festival.domain.Festival;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.util.List;

public record SchoolFestivalResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String imageUrl,
    List<SchoolFestivalArtistResponse> artists
) {

    public static SchoolFestivalResponse of(Festival festival, List<SchoolFestivalArtistResponse> artists) {
        return new SchoolFestivalResponse(
            festival.getId(),
            festival.getName(),
            festival.getStartDate(),
            festival.getEndDate(),
            festival.getThumbnail(),
            artists
        );
    }

    @QueryProjection
    public SchoolFestivalResponse {
    }
}
