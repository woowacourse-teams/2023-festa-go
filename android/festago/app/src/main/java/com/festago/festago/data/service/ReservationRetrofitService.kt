package com.festago.festago.data.service

import com.festago.festago.data.dto.ReservationTicketsResponse
import com.festago.festago.data.dto.ReservedTicketRequest
import com.festago.festago.data.dto.ReservedTicketResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationRetrofitService {
    @GET("/stages/{stageId}/tickets")
    suspend fun getTickets(
        @Path("stageId") stageId: Int,
    ): Response<ReservationTicketsResponse>

    @POST("/member-tickets")
    suspend fun postReserveTicket(
        @Body ticketId: ReservedTicketRequest,
    ): Response<ReservedTicketResponse>
}
