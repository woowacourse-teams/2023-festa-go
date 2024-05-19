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

        if (response.code() == 401) {
            throw UnauthorizedException()
        }

        if (response.code() == 400) {
            println(response.errorBody())
            response.errorBody()?.string()?.let {
                if (it.contains("BOOKMARK_LIMIT_EXCEEDED")) {
                    return Result.failure(BookmarkLimitExceededException)
                }
            }
            return Result.failure(Throwable("400 Bad Request"))
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
        if (e is UnknownHostException) {
            return Result.failure(NetworkException)
        }
        return Result.failure(e)
    }
}
