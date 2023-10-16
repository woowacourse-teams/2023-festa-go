package com.festago.festago.data.di

import com.festago.festago.data.repository.ReservationTicketDefaultRepository
import com.festago.festago.data.repository.SchoolDefaultRepository
import com.festago.festago.data.repository.StudentVerificationDefaultRepository
import com.festago.festago.data.repository.UserDefaultRepository
import com.festago.festago.repository.ReservationTicketRepository
import com.festago.festago.repository.SchoolRepository
import com.festago.festago.repository.StudentVerificationRepository
import com.festago.festago.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
interface ViewModelScopeModule {

    @Binds
    @ViewModelScoped
    fun bindsReservationTicketDefaultRepository(reservationTicketRepository: ReservationTicketDefaultRepository): ReservationTicketRepository

    @Binds
    @ViewModelScoped
    fun bindsStudentVerificationDefaultRepository(studentVerificationRepository: StudentVerificationDefaultRepository): StudentVerificationRepository

    @Binds
    @ViewModelScoped
    fun bindsUserDefaultRepository(userRepository: UserDefaultRepository): UserRepository

    @Binds
    @ViewModelScoped
    fun bindsSelectSchoolRepository(schoolRepository: SchoolDefaultRepository): SchoolRepository
}
