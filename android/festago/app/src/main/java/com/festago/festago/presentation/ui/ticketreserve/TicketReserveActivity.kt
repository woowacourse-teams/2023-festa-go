package com.festago.festago.presentation.ui.ticketreserve

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.data.repository.ReservationDefaultRepository
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.presentation.model.ReservationStageUiModel
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.customview.OkDialogFragment
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel.Companion.TicketReservationViewModelFactory
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter
import com.festago.festago.presentation.ui.ticketreserve.bottomsheet.TicketReserveBottomSheetFragment

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
        is TicketReserveEvent.ReserveTicketSuccess -> handleReserveTicketSuccess()
        is TicketReserveEvent.ReserveTicketFailed -> handleReserveTicketFailed()
    }

    private fun handleShowTicketTypes(reservationStage: ReservationStageUiModel) {
        TicketReserveBottomSheetFragment.newInstance(reservationStage)
            .show(supportFragmentManager, TicketReserveBottomSheetFragment::class.java.name)
    }

    private fun handleReserveTicketSuccess() {
        OkDialogFragment.newInstance("예약이 완료되었습니다.")
            .show(supportFragmentManager, OkDialogFragment::class.java.name)
    }

    private fun handleReserveTicketFailed() {
        OkDialogFragment.newInstance("예약에 실패하였습니다.")
            .show(supportFragmentManager, OkDialogFragment::class.java.name)
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

    companion object {
        private const val KEY_FESTIVAL_ID = "KEY_FESTIVAL_ID"

        fun getIntent(context: Context, festivalId: Long): Intent {
            return Intent(context, TicketReserveActivity::class.java).apply {
                putExtra(KEY_FESTIVAL_ID, festivalId)
            }
        }
    }
}
