package com.festago.festago.data.dto

import com.festago.festago.domain.model.ReservationTicket

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
