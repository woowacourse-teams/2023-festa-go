package com.festago.festago.data.service

import com.festago.festago.data.dto.artist.ArtistSearchResponse
import com.festago.festago.data.dto.festival.FestivalSearchResponse
import com.festago.festago.data.dto.school.SchoolSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRetrofitService {
    @GET("api/v1/search/festivals")
    suspend fun searchFestivals(
        @Query("keyword") keyword: String,
    ): Response<List<FestivalSearchResponse>>

    @GET("api/v1/search/artists")
    suspend fun searchArtists(
        @Query("keyword") keyword: String,
    ): Response<List<ArtistSearchResponse>>

    @GET("api/v1/search/schools")
    suspend fun searchSchools(
        @Query("keyword") keyword: String,
    ): Response<List<SchoolSearchResponse>>
}
