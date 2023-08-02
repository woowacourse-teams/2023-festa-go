package com.festago.festago.data.repository

import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode
import com.festago.festago.domain.repository.TicketRepository

class TicketDefaultRepository(
    private val ticketRetrofitService: TicketRetrofitService,
) : TicketRepository {

    override suspend fun loadTicket(ticketId: Long): Result<Ticket> {
        val response = ticketRetrofitService.getTicket(ticketId)
        if (response.isSuccessful && response.body() != null) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(
            Throwable("code: ${response.code()}, body: ${response.errorBody()?.toString()}}"),
        )
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

    override suspend fun loadTicketCode(ticketId: Long): Result<TicketCode> {
        val response = ticketRetrofitService.getTicketCode(ticketId)
        if (response.isSuccessful && response.body() != null) {
            return Result.success(response.body()!!.toDomain())
        }
        return Result.failure(
            Throwable("code: ${response.code()}, body: ${response.errorBody()?.toString()}}"),
        )
    }
}
