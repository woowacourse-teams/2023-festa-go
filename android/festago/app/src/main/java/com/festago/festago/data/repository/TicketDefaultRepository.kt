package com.festago.festago.data.repository

import com.festago.festago.data.dto.ReservedTicketRequest
import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runCatchingResponse
import com.festago.festago.model.ReservedTicket
import com.festago.festago.model.Ticket
import com.festago.festago.model.TicketCode
import com.festago.festago.repository.TicketRepository
import javax.inject.Inject

class TicketDefaultRepository @Inject constructor(
    private val ticketRetrofitService: TicketRetrofitService,
) : TicketRepository {

    override suspend fun loadTicket(ticketId: Long): Result<Ticket> =
        runCatchingResponse { ticketRetrofitService.getTicket(ticketId) }
            .onSuccessOrCatch { it.toDomain() }

    override suspend fun loadCurrentTickets(): Result<List<Ticket>> =
        runCatchingResponse { ticketRetrofitService.getCurrentTickets() }
            .onSuccessOrCatch { it.toDomain() }

    override suspend fun loadTicketCode(ticketId: Long): Result<TicketCode> =
        runCatchingResponse { ticketRetrofitService.getTicketCode(ticketId) }
            .onSuccessOrCatch { it.toDomain() }

    override suspend fun loadHistoryTickets(size: Int): Result<List<Ticket>> =
        runCatchingResponse { ticketRetrofitService.getHistoryTickets(size) }
            .onSuccessOrCatch { it.toDomain() }

    override suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket> =
        runCatchingResponse {
            ticketRetrofitService.postReserveTicket(
                ReservedTicketRequest(ticketId),
            )
        }.onSuccessOrCatch { it.toDomain() }
}
