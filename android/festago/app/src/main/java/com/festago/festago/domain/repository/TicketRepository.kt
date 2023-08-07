package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode

interface TicketRepository {
    suspend fun loadTicket(ticketId: Long): Result<Ticket>
    suspend fun loadTickets(): Result<List<Ticket>>
    suspend fun loadTicketCode(ticketId: Long): Result<TicketCode>
}
