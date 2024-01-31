package com.festago.admin.dto;

import com.festago.festival.dto.command.FestivalCreateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record FestivalV1CreateRequest(
    @NotBlank(message = "name은 공백일 수 없습니다.")
    String name,

    @NotNull(message = "startDate는 null 일 수 없습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,

    @NotNull(message = "endDate는 null 일 수 없습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,

    @Nullable
    String thumbnail,

    @NotNull(message = "schoolId는 null 일 수 없습니다.")
    Long schoolId
) {

    public FestivalCreateCommand toCommand() {
        return new FestivalCreateCommand(
            name,
            startDate,
            endDate,
            thumbnail,
            schoolId
        );
    }
}
