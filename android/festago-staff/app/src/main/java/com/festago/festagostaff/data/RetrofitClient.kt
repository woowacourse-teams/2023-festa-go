package com.festago.festagostaff.data

import com.festago.festagostaff.data.service.TicketRetrofitService
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
            val mockWeb = MockWeb()
            return instance ?: createInstance(mockWeb.url).also { instance = it }
        }
    }
}
