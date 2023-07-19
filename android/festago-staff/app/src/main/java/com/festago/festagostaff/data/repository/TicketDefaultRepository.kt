package com.festago.festagostaff.data.repository

import com.festago.festagostaff.data.dto.TicketValidationRequestDto
import com.festago.festagostaff.data.service.TicketRetrofitService
import com.festago.festagostaff.domain.model.TicketState
import com.festago.festagostaff.domain.repository.TicketRepository

class TicketDefaultRepository(
    private val ticketRetrofitService: TicketRetrofitService,
) : TicketRepository {
    override suspend fun validateTicket(code: String): TicketState {
        return ticketRetrofitService
            .validateTicket(TicketValidationRequestDto(code))
            .body()!!
            .toTicketState()
    }
}
