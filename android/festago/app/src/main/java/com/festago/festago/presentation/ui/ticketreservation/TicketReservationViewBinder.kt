package com.festago.festago.presentation.ui.ticketreservation

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.databinding.ActivityTicketReservationBinding
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.ticketreservation.adapter.ReservationAdapter
import com.festago.festago.presentation.ui.ticketreservation.adapter.ReservationHeaderAdapter

class TicketReservationViewBinder(
    private val binding: ActivityTicketReservationBinding,
) {
    private val contentsAdapter = ReservationAdapter()
    private val headerAdapter = ReservationHeaderAdapter()
    private val concatAdapter = ConcatAdapter(headerAdapter, contentsAdapter)

    fun updateUi(uiState: TicketReservationUiState) = when (uiState) {
        is TicketReservationUiState.Loading -> updateLoading()
        is TicketReservationUiState.Success -> updateSuccess(uiState.reservations)
        is TicketReservationUiState.Error -> updateError()
    }

    private fun updateLoading() {
        binding.pbLoading.visibility = VISIBLE
        binding.rvTicketReservation.visibility = INVISIBLE
        binding.tvEmpty.visibility = INVISIBLE
    }

    private fun updateSuccess(reservations: List<ReservationUiModel>) {
        binding.pbLoading.visibility = INVISIBLE
        binding.rvTicketReservation.visibility = VISIBLE
        binding.tvEmpty.visibility = INVISIBLE

        binding.rvTicketReservation.adapter = concatAdapter
        contentsAdapter.submitList(reservations)
    }

    private fun updateError() {
        binding.pbLoading.visibility = INVISIBLE
        binding.rvTicketReservation.visibility = INVISIBLE
        binding.tvEmpty.visibility = VISIBLE
    }
}
