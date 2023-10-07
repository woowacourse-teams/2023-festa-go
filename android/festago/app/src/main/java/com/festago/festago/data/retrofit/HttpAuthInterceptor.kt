package com.festago.festago.data.retrofit

import com.festago.festago.data.datasource.TokenDataSource
import okhttp3.Interceptor
import okhttp3.Response

class HttpAuthInterceptor(private val tokenDateSource: TokenDataSource) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(request = getNewRequest(chain))
        if (isResponseSuccess(response)) {
            response.close()

            return chain.proceed(request = getNewRequest(chain))
        }
        return response
    }

    private fun isResponseSuccess(response: Response) = response.code == RESPONSE_CODE_SUCCESS

    private fun getNewRequest(chain: Interceptor.Chain) = chain.request()
        .newBuilder()
        .addHeader(HEADER_AUTHORIZATION, AUTHORIZATION_TOKEN_FORMAT.format(tokenDateSource.token))
        .build()

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val AUTHORIZATION_TOKEN_FORMAT = "Bearer %s"
        private const val RESPONSE_CODE_SUCCESS = 200
    }
}
