package com.festago.festago.data.dto.bookmark

import com.festago.festago.data.dto.festival.FestivalResponse
import com.festago.festago.domain.model.bookmark.FestivalBookmark
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class FestivalBookmarkResponse(
    val festival: FestivalResponse,
    val createdAt: String,
) {
    fun toDomain() = FestivalBookmark(
        festival.toDomain(),
        createdAt = LocalDateTime.parse(createdAt),
    )
}
