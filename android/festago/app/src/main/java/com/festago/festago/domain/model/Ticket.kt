package com.festago.festago.domain.model

import java.time.LocalDateTime

data class Ticket(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val state: TicketState,
    val stage: Stage,
)
