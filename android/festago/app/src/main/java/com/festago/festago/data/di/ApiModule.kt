package com.festago.festago.data.di

import com.festago.festago.BuildConfig
import com.festago.festago.data.retrofit.AuthInterceptor
import com.festago.festago.data.retrofit.TokenManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofitQualifier

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(AuthInterceptor(tokenManager))
        .build()

    @Provides
    @Singleton
    @NormalRetrofitQualifier
    fun providesNormalRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    @AuthRetrofitQualifier
    fun providesAuthRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun providesBaseUrl(): String = BuildConfig.BASE_URL
}
