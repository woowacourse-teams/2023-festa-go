package com.festago.artist.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record StageStartTime(
    LocalDateTime startTime,
    Long stageId
) {

    @QueryProjection
    public StageStartTime {
    }
}
