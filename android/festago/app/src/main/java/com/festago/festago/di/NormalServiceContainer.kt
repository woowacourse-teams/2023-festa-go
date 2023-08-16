package com.festago.festago.di

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.festago.festago.data.service.TokenRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class NormalServiceContainer(baseUrl: String) {
    private val normalRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    val festivalRetrofitService: FestivalRetrofitService by lazy {
        normalRetrofit.create(FestivalRetrofitService::class.java)
    }

    val tokenRetrofitService: TokenRetrofitService by lazy {
        normalRetrofit.create(TokenRetrofitService::class.java)
    }

    val reservationTicketRetrofitService: ReservationTicketRetrofitService by lazy {
        normalRetrofit.create(ReservationTicketRetrofitService::class.java)
    }
}
