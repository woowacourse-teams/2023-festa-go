package com.festago.festago.data.util

import retrofit2.Response

fun <T> Response<T>.runCatchingWithErrorHandler(): Result<T> {
    try {
        if (this.isSuccessful && this.body() != null) {
            return Result.success(this.body()!!)
        }

        return Result.failure(
            Throwable(
                "{" +
                        "code: ${this.code()}," +
                        "message: ${this.message()}, " +
                        "body: ${this.errorBody()?.string()}" +
                        "}",
            ),
        )
    } catch (e: Exception) {
        return Result.failure(e)
    }
}

suspend fun <T> runCatchingResponse(
    block: suspend () -> Response<T>,
): Result<T> {
    try {
        val response = block()
        if (response.isSuccessful && response.body() != null) {
            return Result.success(response.body()!!)
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
