package com.festago.festival.dto;

import com.festago.festival.domain.Festival;
import java.time.LocalDate;

public record FestivalResponse(
    Long id,
    Long schoolId,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail) {

    public static FestivalResponse from(Festival festival) {
        return new FestivalResponse(
            festival.getId(),
            festival.getSchool().getId(),
            festival.getName(),
            festival.getStartDate(),
            festival.getEndDate(),
            festival.getThumbnail());
    }
}
