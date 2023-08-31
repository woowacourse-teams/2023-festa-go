package com.festago.festago.presentation.ui.tickethistory

import com.festago.festago.model.Stage
import java.time.LocalDateTime

data class TicketHistoryItemUiState(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val reserveAt: LocalDateTime,
    val stage: Stage,
    val festivalId: Int,
    val festivalName: String,
    val festivalThumbnail: String,
)
