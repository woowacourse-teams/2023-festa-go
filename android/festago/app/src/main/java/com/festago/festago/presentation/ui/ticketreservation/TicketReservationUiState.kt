package com.festago.festago.presentation.ui.ticketreservation

import com.festago.festago.presentation.model.ReservationUiModel

sealed class TicketReservationUiState {
    object Loading : TicketReservationUiState()
    class Success(val reservations: List<ReservationUiModel>) : TicketReservationUiState()
    object Error : TicketReservationUiState()
}
