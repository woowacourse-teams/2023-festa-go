package com.festago.festago.data.service

import com.festago.festago.data.dto.TicketCodeDto
import com.festago.festago.data.dto.TicketDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketRetrofitService {
    @GET("/tickets/{id}")
    suspend fun getTicket(
        @Path("id") ticketId: Long,
    ): Response<TicketDto>

    @POST("/tickets/{id}/qr")
    suspend fun getTicketCode(
        @Path("id") ticketId: Long,
    ): Response<TicketCodeDto>
}
