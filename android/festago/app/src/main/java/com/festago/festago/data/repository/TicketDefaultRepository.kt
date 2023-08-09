package com.festago.festago.data.repository

import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode
import com.festago.festago.domain.repository.TicketRepository

class TicketDefaultRepository(
    private val ticketRetrofitService: TicketRetrofitService,
) : TicketRepository {

    override suspend fun loadTicket(ticketId: Long): Result<Ticket> {
        try {
            val response = ticketRetrofitService.getTicket(ticketId)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loadTickets(): Result<List<Ticket>> {
        try {
            val response = ticketRetrofitService.getTickets()
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loadTicketCode(ticketId: Long): Result<TicketCode> {
        try {
            val response = ticketRetrofitService.getTicketCode(ticketId)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun loadAllTickets(size: Int): Result<List<Ticket>> {
        try {
            val response = ticketRetrofitService.getHistoryTickets(size)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
