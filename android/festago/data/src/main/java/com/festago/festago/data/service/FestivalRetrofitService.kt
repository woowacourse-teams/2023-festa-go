package com.festago.festago.data.service

import com.festago.festago.data.dto.festival.FestivalDetailResponse
import com.festago.festago.data.dto.festival.FestivalsResponse
import com.festago.festago.data.dto.festival.PopularFestivalsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface FestivalRetrofitService {
    @GET("api/v1/popular/festivals")
    suspend fun getPopularFestivals(): Response<PopularFestivalsResponse>

    @GET("api/v1/festivals")
    suspend fun getFestivals(
        @Query("region") region: String?,
        @Query("filter") filter: String?,
        @Query("lastFestivalId") lastFestivalId: Long?,
        @Query("lastStartDate") lastStartDate: LocalDate?,
        @Query("size") size: Int?,
    ): Response<FestivalsResponse>

    @GET("api/v1/festivals/{festivalId}")
    suspend fun getFestivalDetail(
        @Path("festivalId") festivalId: Long,
    ): Response<FestivalDetailResponse>
}
