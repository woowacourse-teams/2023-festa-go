package com.festago.festago.data.service

import com.festago.festago.data.dto.artist.ArtistDetailResponse
import com.festago.festago.data.dto.festival.FestivalsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface ArtistRetrofitService {

    @GET("api/v1/artists/{artistId}/festivals")
    suspend fun getArtistFestivals(
        @Path("artistId") artistId: Long,
        @Query("size") size: Int?,
        @Query("lastFestivalId") lastFestivalId: Long?,
        @Query("lastStartDate") lastStartDate: LocalDate?,
        @Query("isPast") isPast: Boolean?,
    ): Response<FestivalsResponse>

    @GET("api/v1/artists/{artistId}")
    suspend fun getArtistDetail(
        @Path("artistId") artistId: Long,
    ): Response<ArtistDetailResponse>
}
