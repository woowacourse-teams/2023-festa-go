package com.festago.festago.data.di.singletonscope

import android.content.Context
import androidx.room.Room
import com.festago.festago.data.database.FestagoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesFestagoDatabase(
        @ApplicationContext context: Context,
    ): FestagoDatabase = Room.databaseBuilder(
        context,
        FestagoDatabase::class.java,
        "festago-database",
    ).build()
}
