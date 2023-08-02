package com.festago.festago.presentation.ui.ticketreserve

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.analytics.FirebaseAnalyticsHelper
import com.festago.festago.data.RetrofitClient
import com.festago.festago.data.repository.ReservationDefaultRepository
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.domain.model.ReservedTicket
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.model.ReservationTicketUiModel
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.customview.OkDialogFragment
import com.festago.festago.presentation.ui.reservationcomplete.ReservationCompleteActivity
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ReserveTicketFailed
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ReserveTicketSuccess
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ShowTicketTypes
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel.Companion.TicketReservationViewModelFactory
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter
import com.festago.festago.presentation.ui.ticketreserve.bottomsheet.TicketReserveBottomSheetFragment

class TicketReserveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketReserveBinding

    private val vm: TicketReserveViewModel by viewModels {
        TicketReservationViewModelFactory(
            ReservationDefaultRepository(
                reservationRetrofitService = RetrofitClient.getInstance().reservationRetrofitService,
            ),
            FirebaseAnalyticsHelper.getInstance(),
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
        is ShowTicketTypes -> handleShowTicketTypes(event.stageId, event.tickets)
        is ReserveTicketSuccess -> handleReserveTicketSuccess(event.reservedTicket)
        is ReserveTicketFailed -> handleReserveTicketFailed()
    }

    private fun handleShowTicketTypes(stageId: Int, tickets: List<ReservationTicketUiModel>) {
        contentsAdapter.currentList.find { it.id == stageId }?.let { stage ->
            TicketReserveBottomSheetFragment.newInstance(stage, tickets)
                .show(supportFragmentManager, TicketReserveBottomSheetFragment::class.java.name)
        }
    }

    private fun handleReserveTicketSuccess(reservedTicket: ReservedTicket) {
        startActivity(
            ReservationCompleteActivity.getIntent(
                this,
                reservedTicket.toPresentation(),
            ),
        )
    }

    private fun handleReserveTicketFailed() {
        OkDialogFragment.newInstance("예약에 실패하였습니다.")
            .show(supportFragmentManager, OkDialogFragment::class.java.name)
    }

    private fun initView() {
        binding.rvTicketReserve.adapter = concatAdapter

        vm.loadReservation(intent.getLongExtra(KEY_FESTIVAL_ID, -1))
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
