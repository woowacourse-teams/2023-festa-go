package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.dao.RecentSearchQueryDao
import com.festago.festago.data.database.FestagoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesRecentSearchQueryDao(database: FestagoDatabase): RecentSearchQueryDao =
        database.recentSearchQueryDao()
}
