package com.festago.festago.data.repository

import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.festago.festago.data.util.onSuccessOrCatch
import com.festago.festago.data.util.runRetrofitWithErrorHandler
import com.festago.festago.model.ReservationTicket
import com.festago.festago.repository.ReservationTicketRepository
import javax.inject.Inject

class ReservationTicketDefaultRepository @Inject constructor(
    private val reservationTicketRetrofitService: ReservationTicketRetrofitService,
) : ReservationTicketRepository {

    override suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>> =
        runRetrofitWithErrorHandler { reservationTicketRetrofitService.getReservationTickets(stageId) }
            .onSuccessOrCatch { it.toDomain() }
}
