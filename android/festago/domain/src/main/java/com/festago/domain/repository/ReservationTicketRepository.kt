package com.festago.domain.repository

import com.festago.festago.domain.model.ReservationTicket

interface ReservationTicketRepository {
    suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>>
}
