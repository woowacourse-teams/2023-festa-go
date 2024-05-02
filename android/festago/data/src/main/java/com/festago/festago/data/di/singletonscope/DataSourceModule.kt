package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.datasource.bookmark.BookmarkDataSource
import com.festago.festago.data.datasource.bookmark.DefaultBookMarkDataSource
import com.festago.festago.data.datasource.token.TokenDataSource
import com.festago.festago.data.datasource.token.TokenLocalDataSource
import com.festago.festago.data.datasource.userinfo.UserInfoDataSource
import com.festago.festago.data.datasource.userinfo.UserInfoLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindsTokenDataSource(tokenDataSource: TokenLocalDataSource): TokenDataSource

    @Binds
    @Singleton
    fun bindsUserInfoDataSource(userInfoDataSource: UserInfoLocalDataSource): UserInfoDataSource

    @Binds
    @Singleton
    fun bindBookmarkDataSource(bookmarkDataSource: DefaultBookMarkDataSource): BookmarkDataSource
}
