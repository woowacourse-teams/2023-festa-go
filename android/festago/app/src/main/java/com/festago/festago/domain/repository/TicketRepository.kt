package com.festago.festago.domain.repository

import com.festago.festago.domain.model.TicketCode

interface TicketRepository {
    suspend fun loadTicketCode(ticketId: Long): TicketCode
}
