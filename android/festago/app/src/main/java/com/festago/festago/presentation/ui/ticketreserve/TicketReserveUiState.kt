package com.festago.festago.presentation.ui.ticketreserve

import com.festago.festago.presentation.model.ReservationFestivalUiState
import com.festago.festago.presentation.model.TicketReserveItemUiState

sealed interface TicketReserveUiState {
    object Loading : TicketReserveUiState
    class Success(
        val festival: ReservationFestivalUiState,
        val stages: List<TicketReserveItemUiState>,
    ) : TicketReserveUiState

    object Error : TicketReserveUiState

    val shouldShowLoading: Boolean
        get() = this is Loading

    val shouldShowSuccess: Boolean
        get() = this is Success

    val shouldShowError: Boolean
        get() = this is Error
}
