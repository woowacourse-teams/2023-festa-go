package com.festago.dto;

import com.festago.domain.Festival;
import java.time.LocalDate;
import java.util.Objects;

public record FestivalCreateRequest(String name, LocalDate startDate, LocalDate endDate, String thumbnail) {

    public Festival toEntity() {
        if (Objects.isNull(thumbnail) || thumbnail.isBlank()) {
            return new Festival(name, startDate, endDate);
        }
        return new Festival(name, startDate, endDate, thumbnail);
    }
}
