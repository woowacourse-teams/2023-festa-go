package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class StageUiModel(
    val id: Int = -1,
    val startTime: LocalDateTime = LocalDateTime.MIN,
)
