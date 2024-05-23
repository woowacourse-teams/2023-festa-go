package com.festago.admin.dto.artist;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record AdminArtistV1Response(
    Long id,
    String name,
    String profileImageUrl,
    String backgroundImageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    @QueryProjection
    public AdminArtistV1Response {
    }
}
