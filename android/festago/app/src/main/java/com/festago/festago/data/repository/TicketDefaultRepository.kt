package com.festago.festago.data.repository

import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode
import com.festago.festago.domain.repository.TicketRepository

class TicketDefaultRepository(
    private val ticketRetrofitService: TicketRetrofitService,
) : TicketRepository {

    override suspend fun loadTicket(ticketId: Long): Ticket {
        return ticketRetrofitService.getTicket(ticketId).body()!!.toDomain()
    }

    override suspend fun loadTickets(): Result<List<Ticket>> {
        val response = ticketRetrofitService.getTickets()
        if (response.isSuccessful && response.body() != null) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(
            Throwable("code: ${response.code()}, body: ${response.errorBody()?.toString()}}"),
        )
    }

    override suspend fun loadTicketCode(ticketId: Long): TicketCode {
        return ticketRetrofitService.getTicketCode(ticketId).body()!!.toDomain()
    }
}
