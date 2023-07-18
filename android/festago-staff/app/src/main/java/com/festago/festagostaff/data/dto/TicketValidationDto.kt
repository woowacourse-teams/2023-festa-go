package com.festago.festagostaff.data.dto

import com.festago.festagostaff.domain.model.TicketState
import kotlinx.serialization.Serializable

@Serializable
data class TicketValidationDto(
    val updatedState: String,
) {
    fun toTicketState(): TicketState = when (updatedState) {
        "BEFORE_ENTRY" -> TicketState.BEFORE_ENTRY
        "AFTER_ENTRY" -> TicketState.AFTER_ENTRY
        "AWAY" -> TicketState.AWAY
        else -> throw IllegalArgumentException("Unknown ticket state: $updatedState")
    }
}
