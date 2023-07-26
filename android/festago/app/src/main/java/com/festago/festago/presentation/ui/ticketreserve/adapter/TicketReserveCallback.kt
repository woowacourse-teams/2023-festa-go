package com.festago.festago.presentation.ui.ticketreserve.adapter

import com.festago.festago.presentation.model.ReservationStageUiModel

interface TicketReserveCallback {
    fun onTicketReserveClicked(
        reservationStageUiModel: ReservationStageUiModel,
    )
}
