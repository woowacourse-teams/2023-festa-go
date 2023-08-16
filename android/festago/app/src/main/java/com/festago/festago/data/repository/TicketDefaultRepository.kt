package com.festago.festago.data.repository

import com.festago.domain.repository.TicketRepository
import com.festago.festago.data.dto.ReservedTicketRequest
import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.domain.model.ReservedTicket
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode

class TicketDefaultRepository(
    private val ticketRetrofitService: TicketRetrofitService,
) : TicketRepository {

    override suspend fun loadTicket(ticketId: Long): Result<Ticket> {
        ticketRetrofitService.getTicket(ticketId).runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }

    override suspend fun loadCurrentTickets(): Result<List<Ticket>> {
        ticketRetrofitService.getCurrentTickets().runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }

    override suspend fun loadTicketCode(ticketId: Long): Result<TicketCode> {
        ticketRetrofitService.getTicketCode(ticketId).runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }

    override suspend fun loadHistoryTickets(size: Int): Result<List<Ticket>> {
        ticketRetrofitService.getHistoryTickets(size).runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }

    override suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket> {
        ticketRetrofitService.postReserveTicket(ReservedTicketRequest(ticketId))
            .runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }
}
