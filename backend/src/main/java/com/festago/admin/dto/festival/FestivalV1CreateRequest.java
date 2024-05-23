package com.festago.admin.dto.festival;

import com.festago.festival.dto.command.FestivalCreateCommand;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record FestivalV1CreateRequest(
    @NotBlank
    String name,

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,

    @Nullable
    String posterImageUrl,

    @NotNull
    Long schoolId
) {

    public FestivalCreateCommand toCommand() {
        return FestivalCreateCommand.builder()
            .name(name)
            .startDate(startDate)
            .endDate(endDate)
            .posterImageUrl(posterImageUrl)
            .schoolId(schoolId)
            .build();
    }
}
