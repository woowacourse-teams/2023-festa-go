package com.festago.festago.data.dto

import com.festago.festago.model.ReservationTickets
import kotlinx.serialization.Serializable

@Serializable
data class ReservationTicketsResponse(
    val tickets: List<ReservationTicketResponse>,
) {
    fun toDomain(): ReservationTickets = ReservationTickets(
        tickets.map { it.toDomain() },
    )
}
