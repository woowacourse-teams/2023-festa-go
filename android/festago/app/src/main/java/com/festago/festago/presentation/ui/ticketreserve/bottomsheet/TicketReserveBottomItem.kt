package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import com.festago.festago.presentation.model.ReservationTicketUiModel

data class TicketReserveBottomItem(
    val ticket: ReservationTicketUiModel,
    val isSelected: Boolean = false,
)
