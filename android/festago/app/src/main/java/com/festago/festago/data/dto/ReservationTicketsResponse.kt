package com.festago.festago.data.dto

import com.festago.domain.model.ReservationTicket
import kotlinx.serialization.Serializable

@Serializable
data class ReservationTicketsResponse(
    val tickets: List<ReservationTicketResponse>,
) {
    fun toDomain(): List<ReservationTicket> = tickets.map { it.toDomain() }
}
