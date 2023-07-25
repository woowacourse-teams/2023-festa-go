package com.festago.festago.presentation.ui.ticketreservation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.data.repository.ReservationDefaultRepository
import com.festago.festago.databinding.ActivityTicketReservationBinding
import com.festago.festago.presentation.ui.ticketreservation.TicketReservationViewModel.Companion.TicketReservationViewModelFactory

class TicketReservationActivity : AppCompatActivity() {

    private lateinit var viewBinder: TicketReservationViewBinder

    private val viewModel: TicketReservationViewModel by viewModels {
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
        viewBinder = TicketReservationViewBinder(binding)
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
