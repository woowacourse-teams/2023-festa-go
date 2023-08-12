package com.festago.festago.di

import com.festago.festago.data.repository.AuthDefaultRepository
import com.festago.festago.data.repository.FestivalDefaultRepository
import com.festago.festago.data.repository.ReservationTicketDefaultRepository
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.data.repository.UserDefaultRepository
import com.festago.festago.domain.repository.AuthRepository
import com.festago.festago.domain.repository.FestivalRepository
import com.festago.festago.domain.repository.ReservationTicketRepository
import com.festago.festago.domain.repository.TicketRepository
import com.festago.festago.domain.repository.UserRepository

class RepositoryContainer(
    localDataSourceContainer: LocalDataSourceContainer,
    serviceContainer: ServiceContainer,
) {
    val authRepository: AuthRepository = AuthDefaultRepository(
        authRetrofitService = serviceContainer.authRetrofitService,
        authDataSource = localDataSourceContainer.authDataSource,
        userRetrofitService = serviceContainer.userRetrofitService,
    )

    val festivalRepository: FestivalRepository = FestivalDefaultRepository(
        festivalRetrofitService = serviceContainer.festivalRetrofitService,
    )

    val ticketRepository: TicketRepository = TicketDefaultRepository(
        ticketRetrofitService = serviceContainer.ticketRetrofitService,
    )

    val userRepository: UserRepository = UserDefaultRepository(
        userProfileService = serviceContainer.userRetrofitService,
    )

    val reservationRepository: ReservationTicketRepository = ReservationTicketDefaultRepository(
        reservationTicketRetrofitService = serviceContainer.reservationTicketRetrofitService,
    )
}
