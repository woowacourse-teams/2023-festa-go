package com.festago.festago.presentation.ui.ticketreserve

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.festago.festago.data.repository.ReservationDefaultRepository
import com.festago.festago.databinding.ActivityTicketReserveBinding
import com.festago.festago.presentation.model.ReservationStageUiModel
import com.festago.festago.presentation.model.ReservationTicketUiModel
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel.Companion.TicketReservationViewModelFactory
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveAdapter
import com.festago.festago.presentation.ui.ticketreserve.adapter.TicketReserveHeaderAdapter
import com.festago.festago.presentation.ui.ticketreserve.bottomSheet.TicketReserveBottomSheetFragment
import java.time.LocalDateTime

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

        val fakeReservationTickets = listOf(
            ReservationTicketUiModel(219, "재학생용", 500),
            ReservationTicketUiModel(212, "외부인용", 300),
            ReservationTicketUiModel(212, "외부인용", 300),
            ReservationTicketUiModel(212, "외부인용", 300),
            ReservationTicketUiModel(212, "외부인용", 300),
        )

        val fakeReservationStage = ReservationStageUiModel(
            1,
            "1차",
            LocalDateTime.parse("2021-10-01T00:00:00"),
            LocalDateTime.parse("2021-09-30T00:00:00"),
            fakeReservationTickets,
        )

        val fragment = TicketReserveBottomSheetFragment.newInstance(fakeReservationStage)
        fragment.show(supportFragmentManager, "bottomSheet")
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
