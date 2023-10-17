package com.festago.festago.data.dto

import com.festago.festago.model.ReservationTicket
import com.festago.festago.model.TicketType
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
        ticketType = convertToTicketType(ticketType),
        totalAmount = totalAmount,
        remainAmount = remainAmount,
    )

    private fun convertToTicketType(ticketType: String): TicketType {
        return when (ticketType) {
            "STUDENT" -> TicketType.STUDENT
            "VISITOR" -> TicketType.VISITOR
            else -> TicketType.OTHER
        }
    }
}
