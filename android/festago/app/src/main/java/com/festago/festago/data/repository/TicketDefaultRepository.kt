package com.festago.festago.data.repository

import com.festago.festago.domain.model.TicketCode
import com.festago.festago.domain.repository.TicketRepository

class TicketDefaultRepository : TicketRepository {
    override suspend fun loadTicketCode(ticketId: Long): TicketCode {
        return TicketCode("code", 30)
    }
}
