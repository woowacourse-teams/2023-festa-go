package com.festago.festago.repository

import com.festago.festago.model.ReservedTicket
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCode
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    fun loadTicket(ticketId: Long): Flow<Result<Ticket>>
    suspend fun loadCurrentTickets(): Result<List<Ticket>>
    fun loadTicketCode(ticketId: Long): Flow<Result<TicketCode>>
    suspend fun loadHistoryTickets(size: Int): Result<List<Ticket>>
    suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket>
}
