package com.festago.festago.presentation.ui.ticketreserve

import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter

class TicketReserveViewBinder(
    private val binding: ActivityTicketReserveBinding,
) {
    private val contentsAdapter = TicketReserveAdapter()
    private val headerAdapter = TicketReserveHeaderAdapter()
    private val concatAdapter = ConcatAdapter(headerAdapter, contentsAdapter)

    fun updateUi(uiState: TicketReserveUiState) = when (uiState) {
        is TicketReserveUiState.Loading -> updateLoading()
        is TicketReserveUiState.Success -> updateSuccess(uiState.reservation)
        is TicketReserveUiState.Error -> updateError()
    }

    private fun updateLoading() = Unit

    private fun updateSuccess(reservations: ReservationUiModel) {
        binding.rvTicketReserve.adapter = concatAdapter
        headerAdapter.submitList(listOf(reservations))
        contentsAdapter.submitList(reservations.reservationStages)
    }

    private fun updateError() = Unit
}
