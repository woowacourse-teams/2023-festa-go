package com.festago.festival.dto.command;

import java.time.LocalDate;

public record FestivalCreateCommand(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail,
    Long schoolId
) {

}
