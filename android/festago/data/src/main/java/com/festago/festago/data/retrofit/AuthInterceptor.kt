package com.festago.festago.data.retrofit

import com.festago.festago.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(private val userRepository: UserRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            chain.proceed(request = getNewRequest(chain))
        }
    }

    private suspend fun getNewRequest(chain: Interceptor.Chain): Request =
        chain.request()
            .newBuilder()
            .addHeader(
                HEADER_AUTHORIZATION,
                AUTHORIZATION_TOKEN_FORMAT.format(
                    userRepository.getAccessToken().getOrNull() ?: "TokenIsNull",
                ),
            )
            .build()

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val AUTHORIZATION_TOKEN_FORMAT = "Bearer %s"
    }
}
