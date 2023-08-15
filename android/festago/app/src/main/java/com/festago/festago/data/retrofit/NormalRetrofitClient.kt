package com.festago.festago.data.retrofit

import com.festago.festago.data.service.AuthRetrofitService
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class NormalRetrofitClient(baseUrl: String = "") {

    private val normalRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    val festivalRetrofitService: FestivalRetrofitService by lazy {
        normalRetrofit.create(FestivalRetrofitService::class.java)
    }

    val authRetrofitService: AuthRetrofitService by lazy {
        normalRetrofit.create(AuthRetrofitService::class.java)
    }

    val reservationTicketRetrofitService: ReservationTicketRetrofitService by lazy {
        normalRetrofit.create(ReservationTicketRetrofitService::class.java)
    }
}
