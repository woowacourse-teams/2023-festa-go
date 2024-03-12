package com.festago.admin.dto.festival;

import com.festago.festival.dto.command.FestivalUpdateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record FestivalV1UpdateRequest(
    @NotBlank(message = "name은 공백일 수 없습니다.")
    String name,

    @NotNull(message = "startDate는 null 일 수 없습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,

    @NotNull(message = "endDate는 null 일 수 없습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,

    @Nullable
    String posterImageUrl
) {

    public FestivalUpdateCommand toCommand() {
        return new FestivalUpdateCommand(
            name,
            startDate,
            endDate,
            posterImageUrl
        );
    }
}
