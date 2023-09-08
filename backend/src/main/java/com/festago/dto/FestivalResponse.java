package com.festago.dto;

import com.festago.domain.Festival;
import java.time.LocalDate;

public record FestivalResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail) {

    public static FestivalResponse from(Festival festival) {
        return new FestivalResponse(
            festival.getId(),
            festival.getName(),
            festival.getStartDate(),
            festival.getEndDate(),
            festival.getThumbnail());
    }
}
