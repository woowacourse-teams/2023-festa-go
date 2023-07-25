package com.festago.festago.presentation.ui.ticketreserve

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.data.repository.ReservationDefaultRepository
import com.festago.festago.databinding.ActivityTicketReservationBinding
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel.Companion.TicketReservationViewModelFactory

class TicketReserveActivity : AppCompatActivity() {

    private lateinit var viewBinder: TicketReserveViewBinder

    private val viewModel: TicketReserveViewModel by viewModels {
        TicketReservationViewModelFactory(
            ReservationDefaultRepository(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initObserve()
        initView()
    }

    private fun initBinding() {
        val binding = ActivityTicketReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        viewBinder = TicketReserveViewBinder(binding)
    }

    private fun initObserve() {
        viewModel.uiState.observe(this) { uiState ->
            viewBinder.updateUi(uiState)
        }
    }

    private fun initView() {
        viewModel.loadReservation()
    }
}
