package com.festago.admin.dto.stage;

import com.festago.stage.dto.command.StageUpdateCommand;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record StageV1UpdateRequest(
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startTime,
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime ticketOpenTime,
    @NotNull
    List<Long> artistIds
) {

    public StageUpdateCommand toCommand() {
        return new StageUpdateCommand(
            startTime,
            ticketOpenTime,
            artistIds
        );
    }
}
