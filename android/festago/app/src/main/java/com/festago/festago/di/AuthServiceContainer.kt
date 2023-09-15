package com.festago.festago.di

import com.festago.festago.data.retrofit.AuthInterceptor
import com.festago.festago.data.service.StudentVerificationRetrofitService
import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.service.UserRetrofitService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class AuthServiceContainer(baseUrl: String, tokenContainer: TokenContainer) {

    private val okHttpClient: OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(AuthInterceptor(tokenContainer.tokenManager))
        .build()

    private val authRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    val ticketRetrofitService: TicketRetrofitService by lazy {
        authRetrofit.create(TicketRetrofitService::class.java)
    }

    val userRetrofitService: UserRetrofitService by lazy {
        authRetrofit.create(UserRetrofitService::class.java)
    }

    val studentVerificationRetrofitService: StudentVerificationRetrofitService by lazy {
        authRetrofit.create(StudentVerificationRetrofitService::class.java)
    }
}
