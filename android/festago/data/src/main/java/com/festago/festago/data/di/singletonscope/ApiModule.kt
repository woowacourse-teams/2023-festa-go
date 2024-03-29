package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrlQualifier

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideRetrofitConverterFactory(): retrofit2.Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    @NormalRetrofitQualifier
    fun providesNormalRetrofit(
        @BaseUrlQualifier baseUrl: String,
        converterFactory: retrofit2.Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    @BaseUrlQualifier
    fun providesBaseUrl(): String = BuildConfig.BASE_URL
}
