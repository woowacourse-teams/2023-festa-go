package com.festago.festago.data.repository

import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.domain.model.MemberTicketFestival
import com.festago.festago.domain.model.Stage
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode
import com.festago.festago.domain.model.TicketCondition
import com.festago.festago.domain.repository.TicketRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime

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
        delay(2000)
        return Result.success(
            listOf(
                Ticket(
                    id = 1,
                    number = 8934,
                    entryTime = LocalDateTime.now(),
                    condition = TicketCondition.BEFORE_ENTRY,
                    reserveAt = LocalDateTime.now(),
                    stage = Stage(id = 1, startTime = LocalDateTime.now()),
                    festivalTicket = MemberTicketFestival(
                        id = 4663,
                        name = "테코대학교 무슨 축제 DAY1",
                        thumbnail = "https://images.unsplash.com/photo-1592194996308-7b43878e84a6",
                    ),
                ),
            ),
        )
    }
}
