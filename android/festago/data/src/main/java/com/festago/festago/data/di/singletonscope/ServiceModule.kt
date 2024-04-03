package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.service.ArtistRetrofitService
import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.service.SchoolRetrofitService
import com.festago.festago.data.service.SearchRetrofitService
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

    @Provides
    @Singleton
    fun providesArtistRetrofitService(
        @NormalRetrofitQualifier retrofit: Retrofit,
    ): ArtistRetrofitService {
        return retrofit.create(ArtistRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesSchoolRetrofitService(
        @NormalRetrofitQualifier retrofit: Retrofit,
    ): SchoolRetrofitService {
        return retrofit.create(SchoolRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesSearchRetrofitService(
        @NormalRetrofitQualifier retrofit: Retrofit,
    ): SearchRetrofitService {
        return retrofit.create(SearchRetrofitService::class.java)
    }
}
