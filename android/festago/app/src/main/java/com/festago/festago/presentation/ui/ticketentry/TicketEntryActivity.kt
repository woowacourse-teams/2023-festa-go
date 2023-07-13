package com.festago.festago.presentation.ui.ticketentry

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.festago.festago.data.repository.TicketDefaultRepository
import com.festago.festago.databinding.ActivityTicketEntryBinding
import com.festago.festago.presentation.ui.ticketentry.TicketEntryViewModel.TicketEntryViewModelFactory

class TicketEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketEntryBinding

    private val vm: TicketEntryViewModel by viewModels {
        TicketEntryViewModelFactory(TicketDefaultRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentTicketId = 0L

        initBinding(currentTicketId)
        initView(currentTicketId)
    }

    private fun initBinding(currentTicketId: Long) {
        binding = ActivityTicketEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = vm
        binding.ticketId = currentTicketId
    }

    private fun initView(currentTicketId: Long) {
        vm.loadTicketCode(currentTicketId)
    }
}
