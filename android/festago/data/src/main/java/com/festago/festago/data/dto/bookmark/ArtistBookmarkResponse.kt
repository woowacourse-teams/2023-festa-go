package com.festago.festago.data.dto.bookmark

import com.festago.festago.domain.model.bookmark.ArtistBookmark
import com.festago.festago.domain.model.bookmark.ArtistBookmarkInfo
import java.time.LocalDateTime

data class ArtistBookmarkResponse(
    val artist: ArtistBookmarkInfo,
    val createdAt: LocalDateTime,
) {
    fun toDomain() = ArtistBookmark(
        artist = artist,
        createdAt = createdAt,
    )
}
