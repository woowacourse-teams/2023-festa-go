package com.festago.festago.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReservedTicketRequest(
    val ticketId: Int,
)
