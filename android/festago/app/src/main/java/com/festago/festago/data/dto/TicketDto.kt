package com.festago.festago.data.dto

import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketState
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TicketDto(
    val id: Int,
    val number: Int,
    val entryTime: String,
    val state: String,
    val stage: StageDto,
) {
    fun toDomain(): Ticket {
        return Ticket(
            entryTime = LocalDateTime.parse(entryTime),
            id = id.toLong(),
            number = number,
            stage = stage.toDomain(),
            state = TicketState.valueOf(state),
        )
    }
}
