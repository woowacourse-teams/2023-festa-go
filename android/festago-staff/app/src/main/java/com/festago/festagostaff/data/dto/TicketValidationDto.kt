package com.festago.festagostaff.data.dto

import com.festago.festagostaff.domain.model.TicketState
import kotlinx.serialization.Serializable

@Serializable
data class TicketValidationDto(
    val updatedState: String,
) {
    fun toTicketState(): TicketState = TicketState.of(updatedState)
}
