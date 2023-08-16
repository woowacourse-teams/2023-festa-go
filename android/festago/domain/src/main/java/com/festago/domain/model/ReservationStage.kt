package com.festago.domain.model

import java.time.LocalDateTime

data class ReservationStage(
    val id: Int,
    val lineUp: String,
    val startTime: LocalDateTime,
    val ticketOpenTime: LocalDateTime,
    val reservationTickets: List<ReservationTicket>,
)
