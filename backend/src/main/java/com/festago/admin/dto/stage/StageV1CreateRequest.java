package com.festago.admin.dto.stage;

import com.festago.stage.dto.command.StageCreateCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record StageV1CreateRequest(
    @NotNull
    Long festivalId,
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startTime,
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime ticketOpenTime,
    @NotNull
    List<Long> artistIds
) {

    public StageCreateCommand toCommand() {
        return StageCreateCommand.builder()
            .festivalId(festivalId)
            .startTime(startTime)
            .ticketOpenTime(ticketOpenTime)
            .artistIds(artistIds)
            .build();
    }
}
