package com.festago.festago.domain.exception

/* 에러 코드가 401 일때 예외 */
class UnauthorizedException : Exception()

fun Throwable.isUnauthorized() = this is UnauthorizedException
