package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import com.festago.festago.presentation.ui.ticketreserve.ReservationTicketArg

data class TicketReserveBottomItem(
    val ticket: ReservationTicketArg,
    val isSelected: Boolean = false,
)
