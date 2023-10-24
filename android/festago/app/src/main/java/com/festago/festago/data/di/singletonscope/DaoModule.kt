package com.festago.festago.data.di.singletonscope

import android.content.Context
import com.festago.festago.data.dao.FestagoDatabase
import com.festago.festago.data.dao.FestivalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    fun provideFestivalDao(@ApplicationContext context: Context): FestivalDao =
        FestagoDatabase.buildDatabase(context).festivalDao()
}
