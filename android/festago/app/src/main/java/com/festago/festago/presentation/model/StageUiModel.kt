package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class StageUiModel(
    val id: Long,
    val name: String,
    val startTime: LocalDateTime,
) {
    companion object {
        val EMPTY = StageUiModel(id = 0, name = "", startTime = LocalDateTime.MAX)
    }
}
