package com.festago.festago.data

import com.festago.festago.BuildConfig
import com.festago.festago.data.service.TicketRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RetrofitClient private constructor(
    private val baseUrl: String,
) {
    val ticketRetrofitService: TicketRetrofitService by lazy {
        normalRetrofit.create(TicketRetrofitService::class.java)
    }

    private val normalRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    companion object {
        private var instance: RetrofitClient? = null

        private fun createInstance(baseUrl: String = ""): RetrofitClient {
            return RetrofitClient(baseUrl.removeSuffix("/"))
        }

        @Synchronized
        fun getInstance(): RetrofitClient {
            return instance ?: createInstance(BuildConfig.BASE_URL).also { instance = it }
        }
    }
}
