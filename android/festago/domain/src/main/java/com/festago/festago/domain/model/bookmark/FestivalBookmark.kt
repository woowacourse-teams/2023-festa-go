package com.festago.festago.domain.model.bookmark

import com.festago.festago.domain.model.festival.Festival
import java.time.LocalDateTime

class FestivalBookmark(
    val festival: Festival,
    val createdAt: LocalDateTime
)
