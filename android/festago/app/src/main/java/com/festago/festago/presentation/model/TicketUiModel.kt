package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class TicketUiModel(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val ticketState: TicketStateUiModel,
    val stage: StageUiModel,
) {
    companion object {
        val EMPTY = TicketUiModel(
            id = 0,
            number = 0,
            entryTime = LocalDateTime.MAX,
            ticketState = TicketStateUiModel.EMPTY,
            stage = StageUiModel.EMPTY,
        )
    }
}
