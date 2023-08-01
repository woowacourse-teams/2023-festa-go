package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class TicketUiModel(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val reserveAt: LocalDateTime,
    val condition: TicketConditionUiModel,
    val stage: StageUiModel,
    val festivalId: Int,
    val festivalName: String,
    val festivalThumbnail: String,
) {
    companion object {
        val EMPTY = TicketUiModel(
            id = 0L,
            number = 0,
            entryTime = LocalDateTime.MAX,
            condition = TicketConditionUiModel.EMPTY,
            stage = StageUiModel.EMPTY,
            reserveAt = LocalDateTime.MAX,
            festivalId = 0,
            festivalName = "",
            festivalThumbnail = "",
        )
    }
}
