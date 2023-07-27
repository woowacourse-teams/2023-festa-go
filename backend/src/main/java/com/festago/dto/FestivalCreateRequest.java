package com.festago.dto;

import com.festago.domain.Festival;
import java.time.LocalDate;

public record FestivalCreateRequest(String name, LocalDate startDate, LocalDate endDate, String thumbnail) {


    public Festival toEntity() {
        if (thumbnail == null) {
            return new Festival(name, startDate, endDate);
        }
        return new Festival(name, startDate, endDate, thumbnail);
    }
}
