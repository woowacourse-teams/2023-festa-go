package com.festago.festago.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(HEADER_AUTHORIZATION, AUTHORIZATION_TOKEN_FORMAT.format(tokenProvider()))
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val AUTHORIZATION_TOKEN_FORMAT = "Bearer %s"
    }
}
