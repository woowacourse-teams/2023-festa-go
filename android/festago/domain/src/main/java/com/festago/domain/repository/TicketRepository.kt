package com.festago.domain.repository

import com.festago.domain.model.ReservedTicket
import com.festago.domain.model.Ticket
import com.festago.domain.model.TicketCode

interface TicketRepository {
    suspend fun loadTicket(ticketId: Long): Result<Ticket>
    suspend fun loadCurrentTickets(): Result<List<Ticket>>
    suspend fun loadTicketCode(ticketId: Long): Result<TicketCode>
    suspend fun loadHistoryTickets(size: Int): Result<List<Ticket>>
    suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket>
}
