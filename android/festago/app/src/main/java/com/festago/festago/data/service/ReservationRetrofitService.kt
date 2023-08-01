package com.festago.festago.data.service

import com.festago.festago.data.dto.ReservationFestivalResponse
import com.festago.festago.data.dto.ReservationTicketsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReservationRetrofitService {
    @GET("/festivals/{festivalId}")
    suspend fun getFestival(
        @Path("festivalId") festivalId: Long,
    ): Response<ReservationFestivalResponse>

    //  GET /stages/:stageId/tickets
    @GET("/stages/{stageId}/tickets")
    suspend fun getTickets(
        @Path("stageId") stageId: Int,
    ): Response<ReservationTicketsResponse>
}
