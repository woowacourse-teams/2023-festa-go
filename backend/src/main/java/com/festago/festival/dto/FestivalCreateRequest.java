package com.festago.festival.dto;

import com.festago.festival.domain.Festival;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record FestivalCreateRequest(
    @NotBlank(message = "name 은 공백일 수 없습니다.") String name,
    @NotNull(message = "startDate 는 null 일 수 없습니다.")
    @DateTimeFormat(iso = ISO.DATE)
    LocalDate startDate,
    @NotNull(message = "endDate 는 null 일 수 없습니다.")
    @DateTimeFormat(iso = ISO.DATE)
    LocalDate endDate,
    String thumbnail) {

    public Festival toEntity() {
        if (thumbnail == null || thumbnail.isBlank()) {
            return new Festival(name, startDate, endDate);
        }
        return new Festival(name, startDate, endDate, thumbnail);
    }
}
