package com.festago.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record SchoolBookmarkV1Response(
    Long id,
    String name,
    String logoUrl,
    LocalDateTime bookmarkCreatedAt
) {

    @QueryProjection
    public SchoolBookmarkV1Response {
    }
}
