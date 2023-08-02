package com.festago.festago.domain.model

import java.time.LocalDateTime

data class ReservedTicket(
    val entryTime: LocalDateTime,
    val id: Int,
    val number: Int,
)
