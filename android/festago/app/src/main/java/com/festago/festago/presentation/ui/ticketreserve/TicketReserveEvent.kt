package com.festago.festago.presentation.ui.ticketreserve

import com.festago.festago.presentation.model.ReservationTicketUiModel

sealed interface TicketReserveEvent {
    class ShowTicketTypes(val tickets: List<ReservationTicketUiModel>) : TicketReserveEvent
    object ReserveTicketSuccess : TicketReserveEvent
    object ReserveTicketFailed : TicketReserveEvent
}
