package com.festago.festago.data.retrofit

import com.festago.festago.data.datasource.AuthDataSource

class TokenFactory(private val authDataSource: AuthDataSource) {
    val token: String
        get() = authDataSource.token ?: NULL_TOKEN

    companion object {
        private const val NULL_TOKEN = "null"
    }
}
