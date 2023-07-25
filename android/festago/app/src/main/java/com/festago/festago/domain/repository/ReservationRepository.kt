package com.festago.festago.domain.repository

import com.festago.festago.domain.model.Reservation

interface ReservationRepository {
    suspend fun loadReservation(): Result<List<Reservation>>
}
