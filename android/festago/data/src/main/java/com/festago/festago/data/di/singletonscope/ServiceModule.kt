package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.service.FestivalRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Provides
    @Singleton
    fun providesFestivalRetrofitService(
        @NormalRetrofitQualifier retrofit: Retrofit,
    ): FestivalRetrofitService {
        return retrofit.create(FestivalRetrofitService::class.java)
    }
}
