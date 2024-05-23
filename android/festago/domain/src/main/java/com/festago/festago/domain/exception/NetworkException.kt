package com.festago.festago.domain.exception

class NetworkException : Exception()
fun Throwable.isNetworkError() = this is NetworkException
