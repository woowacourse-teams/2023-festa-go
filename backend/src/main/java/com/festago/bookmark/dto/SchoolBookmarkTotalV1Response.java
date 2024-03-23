package com.festago.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record SchoolBookmarkTotalV1Response(
    SchoolBookmarkV1Response school,
    LocalDateTime bookmarkCreatedAt
) {

    @QueryProjection
    public SchoolBookmarkTotalV1Response {
    }
}
