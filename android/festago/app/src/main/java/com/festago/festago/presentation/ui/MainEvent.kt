package com.festago.festago.presentation.ui

import com.festago.festago.presentation.model.TicketUiModel

sealed class MainEvent {
    class OpenTicketEntry(val ticketUiModel: TicketUiModel) : MainEvent()
    object FailToOpenTicketEntry : MainEvent()
    object FailToLoadTicket : MainEvent()
}
