package com.festago.festago.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.festago.festago.FestagoApplication
import com.festago.festago.presentation.ui.home.HomeViewModel
import com.festago.festago.presentation.ui.home.festivallist.FestivalListViewModel
import com.festago.festago.presentation.ui.home.mypage.MyPageViewModel
import com.festago.festago.presentation.ui.home.ticketlist.TicketListViewModel
import com.festago.festago.presentation.ui.signin.SignInViewModel
import com.festago.festago.presentation.ui.ticketentry.TicketEntryViewModel
import com.festago.festago.presentation.ui.tickethistory.TicketHistoryViewModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel

@Suppress("UNCHECKED_CAST")
val FestagoViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    val repositoryContainer = FestagoApplication.repositoryContainer
    val analysisContainer = FestagoApplication.analysisContainer

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TicketHistoryViewModel::class.java) -> TicketHistoryViewModel(
                ticketRepository = repositoryContainer.ticketRepository,
                analyticsHelper = analysisContainer.analyticsHelper,
            )

            modelClass.isAssignableFrom(TicketReserveViewModel::class.java) -> TicketReserveViewModel(
                reservationTicketRepository = repositoryContainer.reservationTicketRepository,
                festivalRepository = repositoryContainer.festivalRepository,
                ticketRepository = repositoryContainer.ticketRepository,
                authRepository = repositoryContainer.authRepository,
                analyticsHelper = analysisContainer.analyticsHelper,
            )

            modelClass.isAssignableFrom(TicketEntryViewModel::class.java) -> TicketEntryViewModel(
                ticketRepository = repositoryContainer.ticketRepository,
                analyticsHelper = analysisContainer.analyticsHelper,
            )

            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(
                authRepository = repositoryContainer.authRepository,
                analyticsHelper = analysisContainer.analyticsHelper,
            )

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                authRepository = repositoryContainer.authRepository,
            )

            modelClass.isAssignableFrom(FestivalListViewModel::class.java) -> FestivalListViewModel(
                festivalRepository = repositoryContainer.festivalRepository,
                analyticsHelper = analysisContainer.analyticsHelper,
            )

            modelClass.isAssignableFrom(MyPageViewModel::class.java) -> MyPageViewModel(
                userRepository = repositoryContainer.userRepository,
                ticketRepository = repositoryContainer.ticketRepository,
                authRepository = repositoryContainer.authRepository,
                analyticsHelper = analysisContainer.analyticsHelper,
            )

            modelClass.isAssignableFrom(TicketListViewModel::class.java) -> TicketListViewModel(
                ticketRepository = repositoryContainer.ticketRepository,
                analyticsHelper = analysisContainer.analyticsHelper,
            )

            else -> throw IllegalArgumentException("ViewModelFactory에 정의되지않은 뷰모델을 생성하였습니다 : ${modelClass.name}")
        } as T
    }
}
