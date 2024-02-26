package com.festago.admin.presentation.v1.dto;

import com.festago.stage.dto.command.StageCreateCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record StageV1CreateRequest(
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startTime,
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime ticketOpenTime,
    @NotNull
    List<Long> artistIds
) {

    public StageCreateCommand toCommand(Long festivalId) {
        return new StageCreateCommand(
            festivalId,
            startTime,
            ticketOpenTime,
            artistIds
        );
    }
}
