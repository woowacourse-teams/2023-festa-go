package com.festago.festago.presentation.ui.ticketreserve

import com.festago.festago.model.ErrorCode
import com.festago.festago.model.ReservationTicket
import com.festago.festago.model.ReservedTicket
import java.time.LocalDateTime

sealed interface TicketReserveEvent {
    class ShowTicketTypes(
        val stageStartTime: LocalDateTime,
        val tickets: List<ReservationTicket>,
    ) : TicketReserveEvent

    class ReserveTicketSuccess(val reservedTicket: ReservedTicket) : TicketReserveEvent
    class ReserveTicketFailed(val errorCode: ErrorCode) : TicketReserveEvent
    object ShowSignIn : TicketReserveEvent
}
