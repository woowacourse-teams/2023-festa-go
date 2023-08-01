package com.festago.festago.domain.model

data class ReservationTicket(
    val id: Int,
    val ticketType: String,
    val remainAmount: Int,
    val totalAmount: Int,
)
