package com.festago.festago.presentation.ui.ticketreserve

import com.festago.festago.model.ReservationTicket
import java.time.LocalDateTime

data class TicketReserveItemUiState(
    val id: Int,
    val lineUp: String,
    val startTime: LocalDateTime,
    val ticketOpenTime: LocalDateTime,
    val reservationTickets: List<ReservationTicket>,
    val canReserve: Boolean,
    val isSigned: Boolean,
    val onShowStageTickets: (stageId: Int, stageStartTime: LocalDateTime) -> Unit,
)
