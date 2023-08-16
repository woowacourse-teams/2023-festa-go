package com.festago.festago.di

import com.festago.festago.data.repository.TokenDefaultRepository
import com.festago.festago.data.retrofit.NormalRetrofitClient
import com.festago.festago.data.retrofit.TokenManager

class TokenContainer(
    normalRetrofitClient: NormalRetrofitClient,
    localDataSourceContainer: LocalDataSourceContainer,
) {
    val tokenRepository = TokenDefaultRepository(
        authRetrofitService = normalRetrofitClient.authRetrofitService,
        tokenLocalDataSource = localDataSourceContainer.tokenDataSource,
    )
    val tokenManager = TokenManager(tokenRepository)
}
