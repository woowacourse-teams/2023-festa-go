package com.festago.festago.data.repository

import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.domain.model.Stage
import com.festago.festago.domain.model.Ticket
import com.festago.festago.domain.model.TicketCode
import com.festago.festago.domain.model.TicketState
import com.festago.festago.domain.repository.TicketRepository
import java.time.LocalDateTime

class TicketDefaultRepository(
    private val ticketRetrofitService: TicketRetrofitService,
) : TicketRepository {

    private val fakeTicket = List(10) {
        Ticket(
            id = it + 1L,
            number = 100,
            LocalDateTime.MAX,
            TicketState.BEFORE_ENTRY,
            Stage(1, "테코대학교 축제 DAY1", LocalDateTime.MAX),
            "",
        )
    }

    override suspend fun loadTicket(ticketId: Long): Ticket {
        return ticketRetrofitService.getTicket(ticketId).body()!!.toDomain()
    }

    override suspend fun loadTickets(): Result<List<Ticket>> {
        return Result.success(fakeTicket)
    }

    override suspend fun loadTicketCode(ticketId: Long): TicketCode {
        return ticketRetrofitService.getTicketCode(ticketId).body()!!.toDomain()
    }
}
