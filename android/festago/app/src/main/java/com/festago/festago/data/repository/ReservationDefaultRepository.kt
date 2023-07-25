package com.festago.festago.data.repository

import com.festago.festago.domain.model.Reservation
import com.festago.festago.domain.repository.ReservationRepository
import kotlinx.coroutines.delay

class ReservationDefaultRepository : ReservationRepository {
    override suspend fun loadReservation(): Result<List<Reservation>> {
        delay(2000)
        return Result.failure(Exception())
        // return Result.success(List(5) { Reservation(1) })
    }
}
