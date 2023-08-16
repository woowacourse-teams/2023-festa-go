package com.festago.festago.data.dto

import com.festago.domain.model.ReservationTicket
import kotlinx.serialization.Serializable

@Serializable
data class ReservationTicketResponse(
    val id: Int,
    val ticketType: String,
    val totalAmount: Int,
    val remainAmount: Int,
) {
    fun toDomain(): ReservationTicket = ReservationTicket(
        id = id,
        ticketType = ticketType,
        totalAmount = totalAmount,
        remainAmount = remainAmount,
    )
}
