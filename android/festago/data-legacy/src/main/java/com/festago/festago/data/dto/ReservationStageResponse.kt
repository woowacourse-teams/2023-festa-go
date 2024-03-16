package com.festago.festago.data.dto

import com.festago.festago.model.ReservationStage
import com.festago.festago.model.ReservationTickets
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ReservationStageResponse(
    val id: Int,
    val startTime: String,
    val ticketOpenTime: String,
    val lineUp: String,
    val tickets: List<ReservationTicketResponse>,
) {
    fun toDomain(): ReservationStage = ReservationStage(
        id = id,
        lineUp = lineUp,
        startTime = LocalDateTime.parse(startTime),
        ticketOpenTime = LocalDateTime.parse(ticketOpenTime),
        reservationTickets = tickets.toDomain(),
    )

    private fun List<ReservationTicketResponse>.toDomain() = ReservationTickets(
        this.map { it.toDomain() },
    )
}
