package com.festago.festago.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberTicketsResponse(
    @SerialName("tickets") val memberTickets: List<MemberTicketResponse>,
) {
    fun toDomain() = memberTickets.map { it.toDomain() }
}
