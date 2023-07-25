package com.festago.festago.presentation.model

import java.time.LocalDateTime

data class ReservationStageUiModel(
    val id: Int,
    val lineUp: String,
    val startTime: LocalDateTime,
    val ticketOpenTime: LocalDateTime,
    val reservationTickets: List<ReservationTicketUiModel>,
)
