package com.festago.festago.domain.repository

import com.festago.festago.domain.model.ReservationTicket

interface ReservationTicketRepository {
    suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>>
}
