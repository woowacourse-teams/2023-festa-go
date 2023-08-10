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

object RetrofitClient {

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

    private lateinit var normalRetrofit: Retrofit
    private lateinit var authRetrofit: Retrofit

    fun init(authDataSource: AuthDataSource, baseUrl: String = "") {
        initNormalRetrofit(baseUrl)
        initAuthRetrofit(authDataSource, baseUrl)
    }

    private fun initNormalRetrofit(baseUrl: String) {
        if (::normalRetrofit.isInitialized) return
        normalRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun initAuthRetrofit(authDataSource: AuthDataSource, baseUrl: String) {
        if (::authRetrofit.isInitialized) return
        val okHttpClient: OkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(authDataSource))
            .build()

        authRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
