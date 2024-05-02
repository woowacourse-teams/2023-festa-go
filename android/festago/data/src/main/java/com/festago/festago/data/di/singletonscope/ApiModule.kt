package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.BuildConfig
import com.festago.festago.data.retrofit.AuthInterceptor
import com.festago.festago.domain.repository.UserRepository
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
annotation class BaseUrlQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofitQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClientQualifier

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
    @AuthOkHttpClientQualifier
    fun provideOkHttpClient(userRepository: UserRepository): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(AuthInterceptor(userRepository))
        .build()

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
    @AuthRetrofitQualifier
    fun providesAuthRetrofit(
        @BaseUrlQualifier baseUrl: String,
        @AuthOkHttpClientQualifier okHttpClient: OkHttpClient,
        converterFactory: retrofit2.Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    @BaseUrlQualifier
    fun providesBaseUrl(): String = BuildConfig.BASE_URL
}
