package com.festago.festago.domain.repository

import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.domain.model.ReservedTicket

interface ReservationRepository {
    suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>>
    suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket>
}
