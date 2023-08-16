package com.festago.domain.model

import java.time.LocalDateTime

data class ReservedTicket(
    val entryTime: LocalDateTime,
    val id: Long,
    val number: Int,
)
