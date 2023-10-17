package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.datasource.TokenDataSource
import com.festago.festago.data.datasource.TokenLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface DataSourceModule {
    @Binds
    @Singleton
    fun bindsLocalTokenDataSource(tokenDataSource: TokenLocalDataSource): TokenDataSource
}
