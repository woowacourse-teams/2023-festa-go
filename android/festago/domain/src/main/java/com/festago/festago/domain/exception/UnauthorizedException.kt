package com.festago.festago.domain.exception

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/* 에러 코드가 401 일때 예외 */
class UnauthorizedException : Exception()

fun <T> Result<T>.isUnauthorized() = exceptionOrNull() is UnauthorizedException

@OptIn(ExperimentalContracts::class)
@SinceKotlin("1.3")
inline fun <T> Result<T>.onUnauthorized(action: () -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isUnauthorized()) action()
    return this
}
