package com.festago.festago.model

import java.time.LocalDateTime

data class Ticket(
    val id: Long,
    val number: Int,
    val entryTime: LocalDateTime,
    val condition: TicketCondition,
    val reserveAt: LocalDateTime,
    val stage: Stage,
    val festivalTicket: MemberTicketFestival,
)
