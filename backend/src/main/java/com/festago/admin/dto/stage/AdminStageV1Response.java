package com.festago.admin.dto.stage;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;

public record AdminStageV1Response(
    Long id,
    LocalDateTime startDateTime,
    LocalDateTime ticketOpenTime,
    List<AdminStageArtistV1Response> artists,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    @QueryProjection
    public AdminStageV1Response {
    }
}
