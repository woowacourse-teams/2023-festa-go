package com.festago.festago.data.service

import com.festago.festago.data.dto.MemberTicketResponse
import com.festago.festago.data.dto.MemberTicketsResponse
import com.festago.festago.data.dto.TicketCodeDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketRetrofitService {
    @GET("/tickets/{id}")
    suspend fun getTicket(
        @Path("id") ticketId: Long,
    ): Response<MemberTicketResponse>

    @GET("/member-tickets")
    suspend fun getTickets(): Response<MemberTicketsResponse>

    @POST("/tickets/{id}/qr")
    suspend fun getTicketCode(
        @Path("id") ticketId: Long,
    ): Response<TicketCodeDto>
}
