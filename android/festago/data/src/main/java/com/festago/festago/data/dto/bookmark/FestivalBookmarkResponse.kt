package com.festago.festago.data.dto.bookmark

import com.festago.festago.data.dto.festival.FestivalResponse
import com.festago.festago.domain.model.bookmark.FestivalBookmark
import java.time.LocalDateTime

class FestivalBookmarkResponse(
    val festival: FestivalResponse,
    val createdAt: LocalDateTime,
) {
    fun toDomain() = FestivalBookmark(
        festival.toDomain(),
        createdAt,
    )
}
