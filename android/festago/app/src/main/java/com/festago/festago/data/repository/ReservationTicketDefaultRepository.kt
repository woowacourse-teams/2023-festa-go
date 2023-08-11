package com.festago.festago.data.repository

import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.festago.festago.data.util.runCatchingWithErrorHandler
import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.domain.repository.ReservationTicketRepository

class ReservationTicketDefaultRepository(
    private val reservationTicketRetrofitService: ReservationTicketRetrofitService,
) : ReservationTicketRepository {

    override suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>> {
        reservationTicketRetrofitService.getReservationTickets(stageId)
            .runCatchingWithErrorHandler()
            .getOrElse { error -> return Result.failure(error) }
            .let { return Result.success(it.toDomain()) }
    }
}
