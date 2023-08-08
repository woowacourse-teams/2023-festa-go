package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class TicketUiModel(
    val id: Long = -1,
    val number: Int = -1,
    val entryTime: LocalDateTime = LocalDateTime.now(),
    val reserveAt: LocalDateTime = LocalDateTime.now(),
    val condition: TicketConditionUiModel = TicketConditionUiModel.BEFORE_ENTRY,
    val stage: StageUiModel = StageUiModel(),
    val festivalId: Int = -1,
    val festivalName: String = "",
    val festivalThumbnail: String = "",
)
