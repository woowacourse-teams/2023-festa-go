package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class StageUiModel(
    val id: Int,
    val startTime: LocalDateTime,
) {
    companion object {
        val EMPTY = StageUiModel(id = 0, startTime = LocalDateTime.MAX)
    }
}
