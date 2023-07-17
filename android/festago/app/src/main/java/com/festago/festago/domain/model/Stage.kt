package com.festago.festago.domain.model

import java.time.LocalDateTime

data class Stage(
    val id: Long,
    val name: String,
    val startTime: LocalDateTime,
)
