package com.festago.festago.presentation.ui.ticketreserve

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.data.repository.ReservationDefaultRepository
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.presentation.model.ReservationStageUiModel
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel.Companion.TicketReservationViewModelFactory
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter
import com.festago.festago.presentation.ui.ticketreserve.bottomSheet.TicketReserveBottomSheetFragment

class TicketReserveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketReserveBinding

    private val vm: TicketReserveViewModel by viewModels {
        TicketReservationViewModelFactory(
            ReservationDefaultRepository(),
        )
    }

    private val contentsAdapter by lazy { TicketReserveAdapter(vm) }
    private val headerAdapter by lazy { TicketReserveHeaderAdapter() }
    private val concatAdapter by lazy { ConcatAdapter(headerAdapter, contentsAdapter) }

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
        vm.event.observe(this) { event ->
            handleEvent(event)
        }
    }

    private fun handleEvent(event: TicketReserveEvent) = when (event) {
        is TicketReserveEvent.ShowTicketTypes -> handleShowTicketTypes(event.reservationStage)
    }

    private fun handleShowTicketTypes(reservationStage: ReservationStageUiModel) {
        TicketReserveBottomSheetFragment.newInstance(reservationStage)
            .show(supportFragmentManager, TicketReserveBottomSheetFragment::class.java.name)
    }

    private fun initView() {
        binding.rvTicketReserve.adapter = concatAdapter
        vm.loadReservation()
    }

    private fun updateUi(uiState: TicketReserveUiState) = when (uiState) {
        is TicketReserveUiState.Loading,
        is TicketReserveUiState.Error,
        -> Unit

        is TicketReserveUiState.Success -> updateSuccess(uiState.reservation)
    }

    private fun updateSuccess(reservations: ReservationUiModel) {
        headerAdapter.submitList(listOf(reservations))
        contentsAdapter.submitList(reservations.reservationStages)
    }
}
