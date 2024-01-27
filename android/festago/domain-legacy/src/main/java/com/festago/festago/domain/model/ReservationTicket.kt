package com.festago.festago.model

data class ReservationTicket(
    val id: Int,
    val ticketType: TicketType,
    val remainAmount: Int,
    val totalAmount: Int,
)
