package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Reservation

interface ReservationRepository {
    suspend fun loadReservation(): Result<Reservation>
    suspend fun reserveTicket(stageId: Int, ticketId: Int): Result<Int>
}
