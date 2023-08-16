package com.festago.festago.repository

import com.festago.festago.model.ReservationTicket

interface ReservationTicketRepository {
    suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>>
}
