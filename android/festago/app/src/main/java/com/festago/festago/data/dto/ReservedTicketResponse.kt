package com.festago.festago.data.dto

import com.festago.festago.domain.model.ReservedTicket
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ReservedTicketResponse(
    val entryTime: String,
    val id: Int,
    val number: Int,
) {
    fun toDomain(): ReservedTicket = ReservedTicket(
        entryTime = LocalDateTime.parse(entryTime),
        id = id,
        number = number,
    )
}
