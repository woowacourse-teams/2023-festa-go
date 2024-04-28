package com.festago.festago.data.util

import com.festago.festago.domain.exception.UnauthorizedException
import retrofit2.Response

suspend fun <T> runCatchingResponse(
    block: suspend () -> Response<T>,
): Result<T> {
    try {
        val response = block()
        if (response.isSuccessful && response.body() != null) {
            return Result.success(response.body()!!)
        }

        if (response.code() == 401) {
            throw UnauthorizedException()
        }

        return Result.failure(
            Throwable(
                "{" +
                    "code: ${response.code()}," +
                    "message: ${response.message()}, " +
                    "body: ${response.errorBody()?.string()}" +
                    "}",
            ),
        )
    } catch (e: Exception) {
        return Result.failure(e)
    }
}
