package com.festago.admin.dto;

import java.time.LocalDate;

public record AdminFestivalResponse(
    Long id,
    Long schoolId,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String thumbnail) {

}
