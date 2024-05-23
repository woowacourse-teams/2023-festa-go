package com.festago.bookmark.dto.v1;

import com.querydsl.core.annotations.QueryProjection;

public record ArtistBookmarkInfoV1Response(
    Long id,
    String name,
    String profileImageUrl
) {

    @QueryProjection
    public ArtistBookmarkInfoV1Response {
    }
}
