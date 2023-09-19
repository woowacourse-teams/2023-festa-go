package com.festago.festago.data.di

import com.festago.festago.analytics.AnalyticsHelper
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface AnalyticsModule {
    @Binds
    @Singleton
    fun bindsFirebaseAnalyticsHelper(
        analyticsHelper: FirebaseAnalyticsHelper
    ): AnalyticsHelper
}
