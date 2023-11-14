package com.festago.festago.presentation.ui.ticketreserve

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.R
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.model.ErrorCode
import com.festago.festago.model.ReservationTicket
import com.festago.festago.model.ReservedTicket
import com.festago.festago.presentation.ui.customview.OkDialogFragment
import com.festago.festago.presentation.ui.reservationcomplete.ReservationCompleteActivity
import com.festago.festago.presentation.ui.reservationcomplete.ReservedTicketArg
import com.festago.festago.presentation.ui.signin.SignInActivity
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ReserveTicketFailed
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ReserveTicketSuccess
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ShowSignIn
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveEvent.ShowTicketTypes
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter
import com.festago.festago.presentation.ui.ticketreserve.bottomsheet.BottomSheetReservationTicketArg
import com.festago.festago.presentation.ui.ticketreserve.bottomsheet.BottomSheetTicketTypeArg
import com.festago.festago.presentation.ui.ticketreserve.bottomsheet.TicketReserveBottomSheetFragment
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class TicketReserveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketReserveBinding

    private val vm: TicketReserveViewModel by viewModels()

    private val contentsAdapter by lazy { TicketReserveAdapter() }
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
        repeatOnStarted(this) {
            vm.uiState.collect { uiState ->
                updateUi(uiState)
                binding.uiState = uiState
            }
        }
        repeatOnStarted(this) {
            vm.event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: TicketReserveEvent) = when (event) {
        is ShowTicketTypes -> handleShowTicketTypes(
            stageStartTime = event.stageStartTime,
            reservationTickets = event.tickets,
        )

        is ReserveTicketSuccess -> handleReserveTicketSuccess(event.reservedTicket)
        is ReserveTicketFailed -> handleReserveTicketFailed(event.errorCode)
        is ShowSignIn -> handleShowSignIn()
    }

    private fun handleShowTicketTypes(
        stageStartTime: LocalDateTime,
        reservationTickets: List<ReservationTicket>,
    ) {
        TicketReserveBottomSheetFragment.newInstance(
            stageStartTime.format(
                DateTimeFormatter.ofPattern(
                    getString(R.string.ticket_reserve_tv_start_time),
                    Locale.KOREA,
                ),
            ),
            reservationTickets.map {
                BottomSheetReservationTicketArg(
                    id = it.id,
                    remainAmount = it.remainAmount,
                    ticketType = BottomSheetTicketTypeArg.from(it.ticketType),
                    totalAmount = it.totalAmount,
                )
            },
        ).show(supportFragmentManager, TicketReserveBottomSheetFragment::class.java.name)
    }

    private fun handleReserveTicketSuccess(reservedTicket: ReservedTicket) {
        val reservedTicketArg = ReservedTicketArg(
            ticketId = reservedTicket.id,
            number = reservedTicket.number,
            entryTime = reservedTicket.entryTime,
        )
        val intent = ReservationCompleteActivity.getIntent(this, reservedTicketArg)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun handleReserveTicketFailed(errorCode: ErrorCode) {
        val message: String = when (errorCode) {
            is ErrorCode.TICKET_SOLD_OUT -> getString(R.string.ticket_reserve_dialog_sold_out)
            is ErrorCode.RESERVE_TICKET_OVER_AMOUNT -> getString(R.string.ticket_reserve_dialog_over_amount)
            is ErrorCode.NEED_STUDENT_VERIFICATION -> getString(R.string.ticket_reserve_dialog_need_student_verification)
            is ErrorCode.UNKNOWN -> getString(R.string.ticket_reserve_dialog_unknown)
        }
        OkDialogFragment.newInstance(message)
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
            binding.srlTicketReserve.isRefreshing = false
        }
    }

    private fun updateUi(uiState: TicketReserveUiState) = when (uiState) {
        is TicketReserveUiState.Loading,
        is TicketReserveUiState.Error,
        -> Unit

        is TicketReserveUiState.Success -> updateSuccess(uiState)
    }

    private fun updateSuccess(successState: TicketReserveUiState.Success) {
        headerAdapter.submitList(listOf(successState.festival))
        contentsAdapter.submitList(successState.stages)
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
