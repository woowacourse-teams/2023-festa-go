package com.festago.festival.dto.command;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FestivalUpdateCommand(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String posterImageUrl
) {

}
