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
    private val serviceContainer: ServiceContainer,
    tokenContainer: TokenContainer,
) {
    val authRepository: AuthRepository = AuthDefaultRepository(
        tokenRepository = tokenContainer.tokenRepository,
        userRetrofitService = serviceContainer.userRetrofitService,
    )

    val festivalRepository: FestivalRepository
        get() = FestivalDefaultRepository(
            festivalRetrofitService = serviceContainer.festivalRetrofitService,
        )

    val ticketRepository: TicketRepository
        get() = TicketDefaultRepository(
            ticketRetrofitService = serviceContainer.ticketRetrofitService,
        )

    val userRepository: UserRepository
        get() = UserDefaultRepository(
            userProfileService = serviceContainer.userRetrofitService,
        )

    val reservationTicketRepository: ReservationTicketRepository
        get() = ReservationTicketDefaultRepository(
            reservationTicketRetrofitService = serviceContainer.reservationTicketRetrofitService,
        )
}
