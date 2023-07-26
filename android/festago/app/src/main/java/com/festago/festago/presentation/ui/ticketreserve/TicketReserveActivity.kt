package com.festago.festago.presentation.ui.ticketreserve

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.data.repository.ReservationDefaultRepository
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel.Companion.TicketReservationViewModelFactory
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter

class TicketReserveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketReserveBinding

    private val vm: TicketReserveViewModel by viewModels {
        TicketReservationViewModelFactory(
            ReservationDefaultRepository(),
        )
    }

    private val contentsAdapter = TicketReserveAdapter()
    private val headerAdapter = TicketReserveHeaderAdapter()
    private val concatAdapter = ConcatAdapter(headerAdapter, contentsAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initObserve()
        initView()
    }

    private fun initBinding() {
        binding = ActivityTicketReserveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
    }

    private fun initObserve() {
        vm.uiState.observe(this) { uiState ->
            updateUi(uiState)
            binding.uiState = uiState
        }
    }

    private fun initView() {
        vm.loadReservation()
    }

    private fun updateUi(uiState: TicketReserveUiState) = when (uiState) {
        is TicketReserveUiState.Loading -> Unit
        is TicketReserveUiState.Success -> updateSuccess(uiState.reservation)
        is TicketReserveUiState.Error -> Unit
    }

    private fun updateSuccess(reservations: ReservationUiModel) {
        binding.rvTicketReserve.adapter = concatAdapter
        headerAdapter.submitList(listOf(reservations))
        contentsAdapter.submitList(reservations.reservationStages)
    }
}
