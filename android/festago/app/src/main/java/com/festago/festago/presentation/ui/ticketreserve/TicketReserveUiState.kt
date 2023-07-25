package com.festago.festago.presentation.ui.ticketreserve

import com.festago.festago.presentation.model.ReservationUiModel

sealed class TicketReserveUiState {
    object Loading : TicketReserveUiState()
    class Success(val reservation: ReservationUiModel) : TicketReserveUiState()
    object Error : TicketReserveUiState()
}
