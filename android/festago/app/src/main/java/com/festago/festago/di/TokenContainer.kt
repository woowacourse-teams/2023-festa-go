package com.festago.festago.di

import com.festago.festago.data.datasource.AuthLocalDataSource
import com.festago.festago.data.retrofit.TokenFactory

class TokenContainer(authLocalDataSource: AuthLocalDataSource) {
    val token = TokenFactory(authLocalDataSource).token
}
