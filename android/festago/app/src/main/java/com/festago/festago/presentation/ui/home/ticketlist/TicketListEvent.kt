package com.festago.festago.presentation.ui.home.ticketlist

sealed interface TicketListEvent {
    class ShowTicketEntry(val ticketId: Long) : TicketListEvent
}
