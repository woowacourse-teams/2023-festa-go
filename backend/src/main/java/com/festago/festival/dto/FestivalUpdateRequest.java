package com.festago.festival.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record FestivalUpdateRequest(
    @NotBlank(message = "name은 공백일 수 없습니다.") String name,
    @NotNull(message = "startDate는 null일 수 없습니다.") @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
    @NotNull(message = "endDate는 null일 수 없습니다.") @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
    String thumbnail
) {

}
