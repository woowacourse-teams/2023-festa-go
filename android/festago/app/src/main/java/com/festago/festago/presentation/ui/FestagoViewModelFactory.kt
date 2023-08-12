package com.festago.festago.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.festago.festago.FestagoApplication
import com.festago.festago.presentation.ui.tickethistory.TicketHistoryViewModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel

val FestagoViewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    val repositoryContainer = FestagoApplication.DependencyContainer.repositoryContainer
    val analysisContainer = FestagoApplication.DependencyContainer.analysisContainer
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

            else -> throw IllegalArgumentException("ViewModelFactory에 정의되지않은 뷰모델을 생성하였습니다 : ${modelClass.name}")
        } as T
    }
}
