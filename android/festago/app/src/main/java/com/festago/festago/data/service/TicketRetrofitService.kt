package com.festago.festago.data.service

import com.festago.festago.data.dto.MemberTicketResponse
import com.festago.festago.data.dto.MemberTicketsResponse
import com.festago.festago.data.dto.TicketCodeDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketRetrofitService {
    @GET("/member-tickets/{memberTicketId}")
    suspend fun getTicket(
        @Path("memberTicketId") ticketId: Long,
    ): Response<MemberTicketResponse>

    @GET("/member-tickets")
    suspend fun getTickets(): Response<MemberTicketsResponse>

    @POST("/member-tickets/{memberTicketId}/qr")
    suspend fun getTicketCode(
        @Path("memberTicketId") ticketId: Long,
    ): Response<TicketCodeDto>
}
