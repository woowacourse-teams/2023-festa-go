package com.festago.festago.presentation.ui.ticketreserve

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.domain.model.ReservedTicket
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.presentation.mapper.toPresentation
import com.festago.festago.presentation.mapper.toTicketReserveItem
import com.festago.festago.presentation.model.ReservationTicketUiModel
import com.festago.festago.presentation.ui.FestagoViewModelFactory
import com.festago.festago.presentation.ui.customview.OkDialogFragment
import com.festago.festago.presentation.ui.reservationcomplete.ReservationCompleteActivity
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ReserveTicketFailed
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ReserveTicketSuccess
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ShowSignIn
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ShowTicketTypes
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter
import com.festago.festago.presentation.ui.ticketreserve.bottomsheet.TicketReserveBottomSheetFragment

class TicketReserveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketReserveBinding

    private val vm: TicketReserveViewModel by viewModels { FestagoViewModelFactory }

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
        is ShowSignIn -> handleShowSignIn()
    }

    private fun handleShowTicketTypes(stageId: Int, tickets: List<ReservationTicketUiModel>) {
        contentsAdapter.currentList.find { it.id == stageId }?.let { stage ->
            TicketReserveBottomSheetFragment.newInstance(stage.toPresentation(), tickets)
                .show(supportFragmentManager, TicketReserveBottomSheetFragment::class.java.name)
        }
    }

    private fun handleReserveTicketSuccess(reservedTicket: ReservedTicket) {
        val intent = ReservationCompleteActivity.getIntent(this, reservedTicket.toPresentation())
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun handleReserveTicketFailed() {
        OkDialogFragment.newInstance("예약에 실패하였습니다.")
            .show(supportFragmentManager, OkDialogFragment::class.java.name)
    }

    private fun handleShowSignIn() {
        startActivity(SignInActivity.getIntent(this))
    }

    private fun initView() {
        binding.rvTicketReserve.adapter = concatAdapter

        vm.loadReservation(intent.getLongExtra(KEY_FESTIVAL_ID, -1))

        binding.srlTicketReserve.setOnRefreshListener {
            vm.loadReservation(
                festivalId = intent.getLongExtra(KEY_FESTIVAL_ID, -1),
                refresh = true,
            )
        }
    }

    private fun updateUi(uiState: TicketReserveUiState) = when (uiState) {
        is TicketReserveUiState.Loading,
        is TicketReserveUiState.Error,
        -> binding.srlTicketReserve.isRefreshing = false

        is TicketReserveUiState.Success -> updateSuccess(uiState)
    }

    private fun updateSuccess(successState: TicketReserveUiState.Success) {
        headerAdapter.submitList(listOf(successState.reservation))
        val contents =
            successState.reservation.reservationStages.toTicketReserveItem(successState.isSigned)
        contentsAdapter.submitList(contents)
        binding.srlTicketReserve.isRefreshing = false
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
