package com.festago.festago.presentation.ui.ticketreserve

import com.festago.festago.domain.model.ReservedTicket
import com.festago.festago.presentation.model.ReservationTicketUiModel

sealed interface TicketReserveEvent {
    class ShowTicketTypes(val tickets: List<ReservationTicketUiModel>) : TicketReserveEvent
    class ReserveTicketSuccess(val reservedTicket: ReservedTicket) : TicketReserveEvent
    object ReserveTicketFailed : TicketReserveEvent
}
