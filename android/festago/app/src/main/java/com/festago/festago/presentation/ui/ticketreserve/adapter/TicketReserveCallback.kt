package com.festago.festago.presentation.ui.ticketreserve.adapter

import com.festago.festago.presentation.model.ReservationStageUiModel

fun interface TicketReserveCallback {
    fun onTicketReserveClick(reservationStage: ReservationStageUiModel)
}
