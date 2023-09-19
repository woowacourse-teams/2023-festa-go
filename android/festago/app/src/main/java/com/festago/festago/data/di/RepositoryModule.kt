package com.festago.festago.data.di

import com.festago.festago.data.repository.AuthDefaultRepository
import com.festago.festago.data.repository.FestivalDefaultRepository
import com.festago.festago.data.repository.ReservationTicketDefaultRepository
import com.festago.festago.data.repository.SchoolDefaultRepository
import com.festago.festago.data.repository.StudentVerificationDefaultRepository
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.data.repository.TokenDefaultRepository
import com.festago.festago.data.repository.UserDefaultRepository
import com.festago.festago.repository.AuthRepository
import com.festago.festago.repository.FestivalRepository
import com.festago.festago.repository.ReservationTicketRepository
import com.festago.festago.repository.SchoolRepository
import com.festago.festago.repository.StudentVerificationRepository
import com.festago.festago.repository.TicketRepository
import com.festago.festago.repository.TokenRepository
import com.festago.festago.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsAuthDefaultRepository(authRepository: AuthDefaultRepository): AuthRepository

    @Binds
    @Singleton
    fun bindsFestivalDefaultRepository(festivalRepository: FestivalDefaultRepository): FestivalRepository

    @Binds
    @Singleton
    fun bindsReservationTicketDefaultRepository(reservationTicketRepository: ReservationTicketDefaultRepository): ReservationTicketRepository

    @Binds
    @Singleton
    fun bindsSchoolDefaultRepository(schoolRepository: SchoolDefaultRepository): SchoolRepository

    @Binds
    @Singleton
    fun bindsStudentVerificationDefaultRepository(studentVerificationRepository: StudentVerificationDefaultRepository): StudentVerificationRepository

    @Binds
    @Singleton
    fun bindsTicketDefaultRepository(ticketRepository: TicketDefaultRepository): TicketRepository

    @Binds
    @Singleton
    fun bindsTokenDefaultRepository(tokenRepository: TokenDefaultRepository): TokenRepository

    @Binds
    @Singleton
    fun binsUserDefaultRepository(userRepository: UserDefaultRepository): UserRepository
}
