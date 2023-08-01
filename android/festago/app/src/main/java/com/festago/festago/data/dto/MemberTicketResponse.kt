package com.festago.festago.data.dto

import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCondition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MemberTicketResponse(
    val id: Long,
    val number: Int,
    val entryTime: String,
    @SerialName("state") val condition: String,
    val reservedAt: String,
    val stage: StageResponse,
    val festival: MemberTicketFestivalResponse,
) {
    fun toDomain(): Ticket = Ticket(
        id = id,
        number = number,
        entryTime = LocalDateTime.parse(entryTime),
        stage = stage.toDomain(),
        reserveAt = LocalDateTime.parse(reservedAt),
        condition = stateToDomain(condition),
        festivalTicket = festival.toDomain(),
    )

    private fun stateToDomain(state: String): TicketCondition {
        return when (state) {
            "BEFORE_ENTRY" -> TicketCondition.BEFORE_ENTRY
            "AFTER_ENTRY" -> TicketCondition.AFTER_ENTRY
            "AWAY" -> TicketCondition.AWAY
            else -> throw IllegalArgumentException("Unknown ticket state: $state")
        }
    }
}
