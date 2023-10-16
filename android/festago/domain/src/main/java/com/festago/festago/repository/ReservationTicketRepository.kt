package com.festago.festago.repository

import com.festago.festago.model.ReservationTickets

interface ReservationTicketRepository {
    suspend fun loadTicketTypes(stageId: Int): Result<ReservationTickets>
}
