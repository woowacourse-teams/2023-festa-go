package com.festago.festago.data.di.singletonscope

import com.festago.festago.data.service.FestivalRetrofitService
import com.festago.festago.data.service.ReservationTicketRetrofitService
import com.festago.festago.data.service.StudentVerificationRetrofitService
import com.festago.festago.data.service.TicketRetrofitService
import com.festago.festago.data.service.TokenRetrofitService
import com.festago.festago.data.service.UserRetrofitService
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
        @NormalRetrofitQualifier retrofit: Retrofit
    ): FestivalRetrofitService {
        return retrofit.create(FestivalRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesTokenRetrofitService(
        @NormalRetrofitQualifier retrofit: Retrofit
    ): TokenRetrofitService {
        return retrofit.create(TokenRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesReservationTicketRetrofitService(
        @NormalRetrofitQualifier retrofit: Retrofit
    ): ReservationTicketRetrofitService {
        return retrofit.create(ReservationTicketRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesTicketRetrofitService(
        @AuthRetrofitQualifier retrofit: Retrofit
    ): TicketRetrofitService {
        return retrofit.create(TicketRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserRetrofitService(
        @AuthRetrofitQualifier retrofit: Retrofit
    ): UserRetrofitService {
        return retrofit.create(UserRetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun providesStudentVerificationRetrofitService(
        @AuthRetrofitQualifier retrofit: Retrofit
    ): StudentVerificationRetrofitService {
        return retrofit.create(StudentVerificationRetrofitService::class.java)
    }
}
