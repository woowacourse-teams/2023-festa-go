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
