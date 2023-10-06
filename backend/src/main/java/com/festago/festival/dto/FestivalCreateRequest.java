package com.festago.festival.dto;

import com.festago.festival.domain.Festival;
import com.festago.school.domain.School;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record FestivalCreateRequest(
    @NotBlank(message = "name은 공백일 수 없습니다.")
    String name,
    @NotNull(message = "startDate는 null 일 수 없습니다.")
    @DateTimeFormat(iso = ISO.DATE)
    LocalDate startDate,
    @NotNull(message = "endDate는 null 일 수 없습니다.")
    @DateTimeFormat(iso = ISO.DATE)
    LocalDate endDate,
    String thumbnail,
    @NotNull(message = "schoolId는 null 일 수 없습니다.")
    Long schoolId) {

    public Festival toEntity(School school) {
        if (thumbnail == null || thumbnail.isBlank()) {
            return new Festival(name, startDate, endDate, school);
        }
        return new Festival(name, startDate, endDate, thumbnail, school);
    }
}
