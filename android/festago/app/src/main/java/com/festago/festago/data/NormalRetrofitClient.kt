package com.festago.festago.data

import com.festago.festago.data.service.AuthRetrofitService
import com.festago.festago.data.service.FestivalRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object NormalRetrofitClient {

    private lateinit var normalRetrofit: Retrofit

    val festivalRetrofitService: FestivalRetrofitService by lazy {
        normalRetrofit.create(FestivalRetrofitService::class.java)
    }

    val authRetrofitService: AuthRetrofitService by lazy {
        normalRetrofit.create(AuthRetrofitService::class.java)
    }

    fun init(baseUrl: String = "") {
        if (::normalRetrofit.isInitialized) return
        normalRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
