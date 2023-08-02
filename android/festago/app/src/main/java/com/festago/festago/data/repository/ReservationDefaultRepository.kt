package com.festago.festago.data.repository

import com.festago.festago.data.dto.ReservedTicketRequest
import com.festago.festago.data.service.ReservationRetrofitService
import com.festago.festago.domain.model.Reservation
import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.domain.model.ReservedTicket
import com.festago.festago.domain.repository.ReservationRepository

class ReservationDefaultRepository(
    private val reservationRetrofitService: ReservationRetrofitService,
) : ReservationRepository {
    override suspend fun loadReservation(festivalId: Long): Result<Reservation> {
        return reservationRetrofitService.getFestival(festivalId).let {
            if (it.isSuccessful && it.body() != null) {
                Result.success(it.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to load reservation"))
            }
        }
    }

    override suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>> {
        return reservationRetrofitService.getTickets(stageId).let {
            if (it.isSuccessful && it.body() != null) {
                Result.success(it.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to load ticket types"))
            }
        }
    }

    override suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket> {
        return reservationRetrofitService.postReserveTicket(ReservedTicketRequest(ticketId)).let {
            if (it.isSuccessful && it.body() != null) {
                Result.success(it.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to reserve ticket"))
            }
        }
    }
}
