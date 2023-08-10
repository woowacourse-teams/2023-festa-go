package com.festago.festago.data.repository

import com.festago.festago.data.dto.ReservedTicketRequest
import com.festago.festago.data.service.ReservationRetrofitService
import com.festago.festago.domain.model.ReservationTicket
import com.festago.festago.domain.model.ReservedTicket
import com.festago.festago.domain.repository.ReservationRepository

class ReservationDefaultRepository(
    private val reservationRetrofitService: ReservationRetrofitService,
) : ReservationRepository {

    override suspend fun loadTicketTypes(stageId: Int): Result<List<ReservationTicket>> {
        try {
            val response = reservationRetrofitService.getTickets(stageId)
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun reserveTicket(ticketId: Int): Result<ReservedTicket> {
        try {
            val response =
                reservationRetrofitService.postReserveTicket(ReservedTicketRequest(ticketId))
            if (response.isSuccessful && response.body() != null) {
                return Result.success(response.body()!!.toDomain())
            }
            return Result.failure(Throwable("code: ${response.code()} message: ${response.message()}"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
