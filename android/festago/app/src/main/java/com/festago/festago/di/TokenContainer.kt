package com.festago.festago.di

import com.festago.festago.data.repository.TokenDefaultRepository
import com.festago.festago.data.retrofit.TokenManager

class TokenContainer(
    normalServiceContainer: NormalServiceContainer,
    localDataSourceContainer: LocalDataSourceContainer,
) {
    val tokenRepository = TokenDefaultRepository(
        tokenRetrofitService = normalServiceContainer.tokenRetrofitService,
        tokenLocalDataSource = localDataSourceContainer.tokenDataSource,
    )
    val tokenManager = TokenManager(tokenRepository)
}
