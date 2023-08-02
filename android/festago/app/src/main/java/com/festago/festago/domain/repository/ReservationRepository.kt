package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Reservation
import com.festago.festago.domain.model.ReservationTicket

interface ReservationRepository {
    suspend fun loadReservation(festivalId: Long): Result<Reservation>
    suspend fun reserveTicket(stageId: Int): Result<List<ReservationTicket>>
}
