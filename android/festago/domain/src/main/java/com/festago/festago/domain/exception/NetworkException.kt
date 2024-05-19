package com.festago.festago.domain.exception

object NetworkException : Exception() {
    private fun readResolve(): Any = NetworkException
}

fun Throwable.isNetworkError() = this is NetworkException
