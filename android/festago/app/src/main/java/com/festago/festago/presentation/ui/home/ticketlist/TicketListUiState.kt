package com.festago.festago.presentation.ui.home.ticketlist

import com.festago.festago.presentation.model.TicketUiModel

sealed interface TicketListUiState {
    object Loading : TicketListUiState

    data class Success(val tickets: List<TicketUiModel>) : TicketListUiState

    object Error : TicketListUiState

    val shouldShowSuccessWithTickets get() = this is Success && tickets.isNotEmpty()
    val shouldShowSuccessAndEmpty get() = this is Success && tickets.isEmpty()
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
