package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class TicketUiModel(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val ticketState: TicketStateUiModel,
    val stage: StageUiModel,
)
