package com.festago.festago.data.repository

import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.domain.repository.ReservationTicketRepository

class ReservationTicketDefaultRepository(
    private val reservationTicketRetrofitService: ReservationTicketRetrofitService,
) : ReservationTicketRepository {

    override suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>> {
        try {
            val response = reservationTicketRetrofitService.getReservationTickets(stageId)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
