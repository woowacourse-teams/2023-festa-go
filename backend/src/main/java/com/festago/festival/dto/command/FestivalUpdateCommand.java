package com.festago.festival.dto.command;

import java.time.LocalDate;

public record FestivalUpdateCommand(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail
) {

}
