package com.festago.festago.data.retrofit

import com.festago.festago.data.datasource.AuthLocalDataSource

class TokenFactory(
    private val authLocalDataSource: AuthLocalDataSource,
) {
    val token: String
        get() = authLocalDataSource.token ?: NULL_TOKEN

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
