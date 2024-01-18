package com.festago.festago.data.service

import com.festago.festago.data.dto.ReservationTicketsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReservationTicketRetrofitService {
    @GET("/stages/{stageId}/tickets")
    suspend fun getReservationTickets(
        @Path("stageId") stageId: Int,
    ): Response<ReservationTicketsResponse>
}
