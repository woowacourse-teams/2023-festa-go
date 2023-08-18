package com.festago.dto;

import java.time.LocalDate;

public record AdminFestivalResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail) {

}
