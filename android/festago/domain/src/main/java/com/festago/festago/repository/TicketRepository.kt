package com.festago.festago.repository

import com.festago.festago.model.ReservedTicket
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCode

interface TicketRepository {
    suspend fun loadTicket(ticketId: Long): Result<Ticket>
    suspend fun loadCurrentTickets(): Result<List<Ticket>>
    suspend fun loadTicketCode(ticketId: Long): Result<TicketCode>
    suspend fun loadHistoryTickets(size: Int): Result<List<Ticket>>
    suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket>
}
