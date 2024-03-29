package com.festago.bookmark.dto.v1;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record SchoolBookmarkV1Response(
    SchoolBookmarkInfoV1Response school,
    LocalDateTime bookmarkCreatedAt
) {

    @QueryProjection
    public SchoolBookmarkV1Response {
    }
}
