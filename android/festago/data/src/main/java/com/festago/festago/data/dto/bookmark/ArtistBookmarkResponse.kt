package com.festago.festago.data.dto.bookmark

import com.festago.festago.domain.model.bookmark.ArtistBookmark
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ArtistBookmarkResponse(
    val artist: ArtistBookmarkInfoResponse,
    val createdAt: String,
) {
    fun toDomain() = ArtistBookmark(
        artist = artist.toDomain(),
        createdAt = LocalDateTime.parse(createdAt),
    )
}
