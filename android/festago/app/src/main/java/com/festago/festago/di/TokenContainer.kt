package com.festago.festago.di

import com.festago.festago.data.datasource.AuthDataSource
import com.festago.festago.data.retrofit.TokenFactory

class TokenContainer(authDataSource: AuthDataSource) {
    val token = TokenFactory(authDataSource).token
}
