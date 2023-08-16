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
    private val authServiceContainer: AuthServiceContainer,
    private val normalServiceContainer: NormalServiceContainer,
    tokenContainer: TokenContainer,
) {
    val authRepository: AuthRepository = AuthDefaultRepository(
        tokenRepository = tokenContainer.tokenRepository,
        userRetrofitService = authServiceContainer.userRetrofitService,
    )

    val festivalRepository: FestivalRepository
        get() = FestivalDefaultRepository(
            festivalRetrofitService = normalServiceContainer.festivalRetrofitService,
        )

    val ticketRepository: TicketRepository
        get() = TicketDefaultRepository(
            ticketRetrofitService = authServiceContainer.ticketRetrofitService,
        )

    val userRepository: UserRepository
        get() = UserDefaultRepository(
            userProfileService = authServiceContainer.userRetrofitService,
        )

    val reservationTicketRepository: ReservationTicketRepository
        get() = ReservationTicketDefaultRepository(
            reservationTicketRetrofitService = normalServiceContainer.reservationTicketRetrofitService,
        )
}
