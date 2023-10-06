package com.festago.festago.data.util

suspend fun <T, R> Result<T>.tryConvert(block: suspend (T) -> R): Result<R> {
    return try {
        onSuccess { return Result.success(block(it)) }
        onFailure { return Result.failure(it) }

        throw Throwable("This line should not be reached")
    } catch (e: Exception) {
        Result.failure(e)
    }
}
