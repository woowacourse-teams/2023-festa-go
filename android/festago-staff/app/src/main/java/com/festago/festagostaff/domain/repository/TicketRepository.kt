package com.festago.festagostaff.domain.repository

import com.festago.festagostaff.domain.model.TicketState

interface TicketRepository {
    suspend fun validateTicket(code: String): TicketState
}
