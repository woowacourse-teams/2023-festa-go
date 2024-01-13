package com.festago.festago.common.analytics.di

import com.festago.festago.common.analytics.AnalyticsHelper
import com.festago.festago.common.analytics.FirebaseAnalyticsHelper
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
        analyticsHelper: FirebaseAnalyticsHelper,
    ): AnalyticsHelper
}
