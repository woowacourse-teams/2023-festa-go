package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.datasource.token.TokenDataSource
import com.festago.festago.data.datasource.token.TokenLocalDataSource
import com.festago.festago.data.datasource.userinfo.UserInfoDataSource
import com.festago.festago.data.datasource.userinfo.UserInfoLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsTokenDataSource(tokenDataSource: TokenLocalDataSource): TokenDataSource

    @Binds
    fun bindsUserInfoDataSource(userInfoDataSource: UserInfoLocalDataSource): UserInfoDataSource
}
