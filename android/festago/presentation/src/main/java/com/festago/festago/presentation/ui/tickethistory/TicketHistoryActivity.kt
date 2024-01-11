package com.festago.festago.presentation.ui.tickethistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.presentation.databinding.ActivityTicketHistoryBinding
import com.festago.festago.presentation.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketHistoryBinding

    private val vm: TicketHistoryViewModel by viewModels()

    private var adapter: TicketHistoryAdapter = TicketHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initObserve()
        initView()
    }

    private fun initBinding() {
        binding = ActivityTicketHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
    }

    private fun initObserve() {
        repeatOnStarted(this) {
            vm.uiState.collect { uiState ->
                when (uiState) {
                    is TicketHistoryUiState.Loading,
                    is TicketHistoryUiState.Error,
                    -> Unit

                    is TicketHistoryUiState.Success -> {
                        adapter.submitList(uiState.tickets)
                    }
                }
                binding.uiState = uiState
            }
        }
    }

    private fun initView() {
        binding.rvTicketHistory.adapter = adapter
        vm.loadTicketHistories()
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, TicketHistoryActivity::class.java)
        }
    }
}
