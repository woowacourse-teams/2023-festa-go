package com.festago.bookmark.dto.v1;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record ArtistBookmarkV1Response(
    ArtistBookmarkInfoV1Response artist,
    LocalDateTime createdAt
) {

    @QueryProjection
    public ArtistBookmarkV1Response {
    }
}
