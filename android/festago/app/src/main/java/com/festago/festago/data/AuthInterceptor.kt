package com.festago.festago.data

import android.accounts.AuthenticatorException
import com.festago.festago.data.datasource.AuthDataSource
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authDataSource: AuthDataSource) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = authDataSource.token ?: throw AuthenticatorException(ERROR_AUTH_NO_TOKEN)
        val request = chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken").build()
        return chain.proceed(request)
    }

    companion object {
        private const val ERROR_AUTH_NO_TOKEN = "저장된 토큰이 없습니다."
    }
}
