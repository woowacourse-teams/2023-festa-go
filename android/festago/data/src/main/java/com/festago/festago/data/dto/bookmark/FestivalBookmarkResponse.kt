package com.festago.festago.data.dto.bookmark

import com.festago.festago.data.dto.festival.FestivalResponse
import java.time.LocalDateTime

class FestivalBookmarkResponse(
    val festival: FestivalResponse,
    val createdAt: LocalDateTime
)
