package com.festago.festago.data.service

import com.festago.festago.data.dto.FestivalsResponse
import com.festago.festago.data.dto.ReservationFestivalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FestivalRetrofitService {
    @GET("/festivals")
    suspend fun getFestivals(
        @Query("festivalFilter") festivalFilter: String,
    ): Response<FestivalsResponse>

    @GET("/festivals/{festivalId}")
    suspend fun getFestivalDetail(
        @Path("festivalId") festivalId: Long,
    ): Response<ReservationFestivalResponse>
}
