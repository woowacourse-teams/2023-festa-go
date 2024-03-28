package com.festago.bookmark.dto.v1;

import com.festago.school.dto.v1.SchoolSearchV1Response;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record SchoolBookmarkV1Response(
    SchoolSearchV1Response school,
    LocalDateTime bookmarkCreatedAt
) {

    @QueryProjection
    public SchoolBookmarkV1Response {
    }
}
