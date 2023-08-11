package com.festago.festago.data.retrofit

import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.service.UserRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object AuthRetrofitClient {

    private lateinit var authRetrofit: Retrofit

    val ticketRetrofitService: TicketRetrofitService by lazy {
        authRetrofit.create(TicketRetrofitService::class.java)
    }

    val userRetrofitService: UserRetrofitService by lazy {
        authRetrofit.create(UserRetrofitService::class.java)
    }
    fun init(baseUrl: String = "", tokenProvider: () -> String) {
        if (::authRetrofit.isInitialized) return
        authRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(tokenProvider))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun getOkHttpClient(tokenProvider: () -> String): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(AuthInterceptor(tokenProvider))
        .build()
}
