package com.festago.festago.data

import com.festago.festago.data.datasource.AuthDataSource
import com.festago.festago.data.service.AuthRetrofitService
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.service.ReservationRetrofitService
import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.service.UserRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class RetrofitClient private constructor(
    private val baseUrl: String,
    authDataSource: AuthDataSource,
) {
    val ticketRetrofitService: TicketRetrofitService by lazy {
        authRetrofit.create(TicketRetrofitService::class.java)
    }

    val reservationRetrofitService: ReservationRetrofitService by lazy {
        authRetrofit.create(ReservationRetrofitService::class.java)
    }

    val festivalRetrofitService: FestivalRetrofitService by lazy {
        normalRetrofit.create(FestivalRetrofitService::class.java)
    }

    val userRetrofitService: UserRetrofitService by lazy {
        authRetrofit.create(UserRetrofitService::class.java)
    }

    val authRetrofitService: AuthRetrofitService by lazy {
        normalRetrofit.create(AuthRetrofitService::class.java)
    }

    private val normalRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private val authRetrofit: Retrofit by lazy {
        val okHttpClient: OkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(authDataSource))
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    companion object {
        private var _instance: RetrofitClient? = null
        val instance: RetrofitClient get() = _instance!!

        fun create(authDataSource: AuthDataSource, baseUrl: String = "") {
            if (_instance == null) {
                _instance = RetrofitClient(baseUrl.removeSuffix("/"), authDataSource)
            }
        }
    }
}
