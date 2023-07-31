package com.festago.festago.presentation.ui.home.ticketlist

import com.festago.festago.presentation.model.TicketUiModel

sealed interface TicketListUiState {
    object Loading : TicketListUiState

    data class Success(val tickets: List<TicketUiModel>) : TicketListUiState {
        val hasTicket get() = tickets.isNotEmpty()
        val hasNotTicket get() = tickets.isEmpty()
    }

    object Error : TicketListUiState

    val shouldShowSuccessWithTickets get() = this is Success && hasTicket
    val shouldShowSuccessAndEmpty get() = this is Success && hasNotTicket
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
