package com.festago.bookmark.dto.v1;

import com.querydsl.core.annotations.QueryProjection;

public record SchoolBookmarkInfoV1Response(
    Long id,
    String name,
    String logoUrl
) {

    @QueryProjection
    public SchoolBookmarkInfoV1Response {
    }
}
