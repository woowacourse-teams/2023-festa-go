package com.festago.festago.presentation.ui.ticketreserve

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
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
        is TicketReserveUiState.Success -> updateSuccess(uiState.reservations)
        is TicketReserveUiState.Error -> updateError()
    }

    private fun updateLoading() {
        binding.pbLoading.visibility = VISIBLE
        binding.rvTicketReserve.visibility = INVISIBLE
        binding.tvEmpty.visibility = INVISIBLE
    }

    private fun updateSuccess(reservations: List<ReservationUiModel>) {
        binding.pbLoading.visibility = INVISIBLE
        binding.rvTicketReserve.visibility = VISIBLE
        binding.tvEmpty.visibility = INVISIBLE

        binding.rvTicketReserve.adapter = concatAdapter
        contentsAdapter.submitList(reservations)
    }

    private fun updateError() {
        binding.pbLoading.visibility = INVISIBLE
        binding.rvTicketReserve.visibility = INVISIBLE
        binding.tvEmpty.visibility = VISIBLE
    }
}
