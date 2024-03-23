package com.festago.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;

public record SchoolBookmarkV1Response(
    Long id,
    String name,
    String logoUrl
) {

    @QueryProjection
    public SchoolBookmarkV1Response {
    }
}
