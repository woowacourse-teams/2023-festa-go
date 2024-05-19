package com.festago.festago.data.util

import com.festago.festago.domain.exception.BookmarkLimitExceededException
import com.festago.festago.domain.exception.NetworkException
import com.festago.festago.domain.exception.UnauthorizedException
import retrofit2.Response
import java.net.UnknownHostException

suspend fun <T> runCatchingResponse(
    block: suspend () -> Response<T>,
): Result<T> {
    try {
        val response = block()
        if (response.isSuccessful && response.body() != null) {
            return Result.success(response.body()!!)
        }

        handleUnauthorizedException(response)

        handleBadRequestException(response)

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
        if (e is UnknownHostException) {
            return Result.failure(NetworkException())
        }
        return Result.failure(e)
    }
}

private fun <T> handleUnauthorizedException(response: Response<T>) {
    if (response.code() == 401) {
        throw UnauthorizedException()
    }
}

private fun <T> handleBadRequestException(response: Response<T>) {
    if (response.code() == 400) {
        response.errorBody()?.string()?.let {
            if (it.contains("BOOKMARK_LIMIT_EXCEEDED")) {
                throw BookmarkLimitExceededException()
            }
        }
    }
}
