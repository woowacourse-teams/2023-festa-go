package com.festago.festago.data.dto

import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketState
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MemberTicketResponse(
    val id: Long,
    val number: Int,
    val entryTime: String,
    val state: String,
    val reservedAt: String,
    val stage: StageResponse,
    val festival: MemberTicketFestivalResponse,
) {
    fun toDomain() = Ticket(
        id = id,
        number = number,
        entryTime = LocalDateTime.parse(entryTime),
        stage = stage.toDomain(),
        reserveAt = LocalDateTime.parse(reservedAt),
        state = stateToDomain(state),
        festivalTicket = festival.toDomain(),
    )

    private fun stateToDomain(state: String): TicketState {
        return when (state) {
            "BEFORE_ENTRY" -> TicketState.BEFORE_ENTRY
            "AFTER_ENTRY" -> TicketState.AFTER_ENTRY
            "AWAY" -> TicketState.AWAY
            else -> throw IllegalArgumentException("Unknown ticket state: $state")
        }
    }
}
