package com.festago.admin.dto.festival;

import com.festago.festival.dto.command.FestivalUpdateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record FestivalV1UpdateRequest(
    @NotBlank
    String name,

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,

    @Nullable
    String posterImageUrl
) {

    public FestivalUpdateCommand toCommand() {
        return FestivalUpdateCommand.builder()
            .name(name)
            .startDate(startDate)
            .endDate(endDate)
            .posterImageUrl(posterImageUrl)
            .build();
    }
}
