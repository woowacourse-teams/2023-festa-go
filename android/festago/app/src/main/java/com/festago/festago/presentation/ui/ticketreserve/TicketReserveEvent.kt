package com.festago.festago.presentation.ui.ticketreserve

import com.festago.domain.model.ReservedTicket
import com.festago.festago.presentation.model.ReservationTicketUiModel
import java.time.LocalDateTime

sealed interface TicketReserveEvent {
    class ShowTicketTypes(
        val stageStartTime: LocalDateTime,
        val tickets: List<ReservationTicketUiModel>,
    ) : TicketReserveEvent

    class ReserveTicketSuccess(val reservedTicket: ReservedTicket) : TicketReserveEvent
    object ReserveTicketFailed : TicketReserveEvent
    object ShowSignIn : TicketReserveEvent
}
