package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.datasource.token.TokenDataSource
import com.festago.festago.data.datasource.token.TokenLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsTokenDataSource(localDataSource: TokenLocalDataSource): TokenDataSource
}
