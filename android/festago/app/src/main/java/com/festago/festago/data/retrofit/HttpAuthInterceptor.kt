package com.festago.festago.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class HttpAuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(request = getNewRequest(chain))
        if (isResponseSuccess(response)) {
            response.close()

            tokenManager.refreshToken()
            return chain.proceed(request = getNewRequest(chain))
        }
        return response
    }

    private fun isResponseSuccess(response: Response) = response.code == RESPONSE_CODE_SUCCESS

    private fun getNewRequest(chain: Interceptor.Chain) = chain.request()
        .newBuilder()
        .addHeader(HEADER_AUTHORIZATION, AUTHORIZATION_TOKEN_FORMAT.format(tokenManager.token))
        .build()

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val AUTHORIZATION_TOKEN_FORMAT = "Bearer %s"
        private const val RESPONSE_CODE_SUCCESS = 200
    }
}
