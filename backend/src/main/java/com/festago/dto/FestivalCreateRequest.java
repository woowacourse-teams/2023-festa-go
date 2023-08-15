package com.festago.dto;

import com.festago.domain.Festival;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record FestivalCreateRequest(
    @NotNull String name,
    @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
    @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
    String thumbnail) {

    public Festival toEntity() {
        if (Objects.isNull(thumbnail) || thumbnail.isBlank()) {
            return new Festival(name, startDate, endDate);
        }
        return new Festival(name, startDate, endDate, thumbnail);
    }
}
