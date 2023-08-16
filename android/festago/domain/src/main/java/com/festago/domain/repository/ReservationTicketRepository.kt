package com.festago.domain.repository

import com.festago.domain.model.ReservationTicket

interface ReservationTicketRepository {
    suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>>
}
