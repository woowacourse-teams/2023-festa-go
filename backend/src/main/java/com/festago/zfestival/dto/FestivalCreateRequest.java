package com.festago.zfestival.dto;

import com.festago.zfestival.domain.Festival;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record FestivalCreateRequest(
    @NotNull(message = "name 은 null 일 수 없습니다.") String name,
    @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
    @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
    String thumbnail) {

    public Festival toEntity() {
        if (thumbnail == null || thumbnail.isBlank()) {
            return new Festival(name, startDate, endDate);
        }
        return new Festival(name, startDate, endDate, thumbnail);
    }
}
