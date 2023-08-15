package com.festago.festago.presentation.ui.tickethistory

import com.festago.festago.presentation.model.TicketUiModel

sealed interface TicketHistoryUiState {
    object Loading : TicketHistoryUiState

    data class Success(val tickets: List<TicketUiModel>) : TicketHistoryUiState {
        val hasTicket get() = tickets.isNotEmpty()
        val hasNotTicket get() = tickets.isEmpty()
    }

    object Error : TicketHistoryUiState

    val shouldShowSuccessWithTickets get() = this is Success && hasTicket
    val shouldShowSuccessAndEmpty get() = this is Success && hasNotTicket
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
