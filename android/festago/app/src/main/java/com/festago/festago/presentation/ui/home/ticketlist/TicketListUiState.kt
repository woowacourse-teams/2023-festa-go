package com.festago.festago.presentation.ui.home.ticketlist

sealed interface TicketListUiState {
    object Loading : TicketListUiState

    data class Success(val tickets: List<TicketListItemUiState>) : TicketListUiState {
        val hasTicket get() = tickets.isNotEmpty()
        val hasNotTicket get() = tickets.isEmpty()
    }

    object Error : TicketListUiState

    val shouldShowSuccessWithTickets get() = this is Success && hasTicket
    val shouldShowSuccessAndEmpty get() = this is Success && hasNotTicket
    val shouldShowLoading get() = this is Loading
    val shouldShowError get() = this is Error
}
