package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.database.FestagoDatabase
import dagger.Provides

internal interface DaosModule {

    @Provides
    fun providesRecentSearchQueryDao(database: FestagoDatabase) = database.recentSearchQueryDao()
}
