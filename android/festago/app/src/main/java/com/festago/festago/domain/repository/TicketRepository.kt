package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode

interface TicketRepository {
    suspend fun loadTicket(ticketId: Long): Ticket
    suspend fun loadTicketCode(ticketId: Long): TicketCode
}
