package com.festago.festago.data.dto

import com.festago.festago.domain.model.ReservationStage
import java.time.LocalDateTime

data class ReservationStageResponse(
    val id: Int,
    val lineUp: String,
    val startTime: String,
    val ticketOpenTime: String,
    val tickets: List<ReservationTicketResponse>,
) {
    fun toDomain(): ReservationStage = ReservationStage(
        id = id,
        lineUp = lineUp,
        startTime = LocalDateTime.parse(startTime),
        ticketOpenTime = LocalDateTime.parse(ticketOpenTime),
        reservationTickets = tickets.map { it.toDomain() },
    )
}
