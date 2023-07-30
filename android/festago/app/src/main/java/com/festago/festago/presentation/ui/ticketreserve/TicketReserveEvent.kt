package com.festago.festago.presentation.ui.ticketreserve

import com.festago.festago.presentation.model.ReservationStageUiModel

sealed interface TicketReserveEvent {
    class ShowTicketTypes(val reservationStage: ReservationStageUiModel) : TicketReserveEvent
}
