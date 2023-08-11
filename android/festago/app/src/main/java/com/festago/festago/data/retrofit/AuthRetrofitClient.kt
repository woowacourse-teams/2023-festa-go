package com.festago.festago.data.retrofit

import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.service.UserRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class AuthRetrofitClient private constructor(
    baseUrl: String,
    okHttpClient: OkHttpClient,
) {

    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val ticketRetrofitService: TicketRetrofitService by lazy {
        authRetrofit.create(TicketRetrofitService::class.java)
    }

    val userRetrofitService: UserRetrofitService by lazy {
        authRetrofit.create(UserRetrofitService::class.java)
    }

    companion object {
        private var _instance: AuthRetrofitClient? = null
        val instance get() = _instance!!

        fun create(baseUrl: String = "", tokenProvider: () -> String) {
            _instance = AuthRetrofitClient(baseUrl, getOkHttpClient(tokenProvider))
        }

        private fun getOkHttpClient(block: () -> String): OkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(block))
            .build()
    }
}
