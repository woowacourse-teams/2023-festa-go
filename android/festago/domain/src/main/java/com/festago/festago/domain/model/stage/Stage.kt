package com.festago.festago.domain.model.stage

import com.festago.festago.domain.model.artist.Artist
import java.time.LocalDateTime

class Stage(
    val id: Long,
    val startDateTime: LocalDateTime,
    val artists: List<Artist>,
)
