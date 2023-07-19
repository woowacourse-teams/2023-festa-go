package com.festago.festago.presentation.ui

import com.festago.festago.R
import com.festago.festago.databinding.ActivityMainBinding
import com.festago.festago.presentation.model.TicketStateUiModel
import com.festago.festago.presentation.model.TicketUiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MainBinding {
    fun setBtnEnterTicket(binding: ActivityMainBinding, ticket: TicketUiModel) {
        val context = binding.root.context
        when (LocalDateTime.now().isAfter(ticket.entryTime)) {
            false -> {
                binding.btnEnterTicket.text =
                    DateTimeFormatter.ofPattern(context.getString(R.string.main_btn_ticket_disable_time))
                        .format(ticket.entryTime)
                binding.btnEnterTicket.isEnabled = false
            }

            else -> {
                binding.btnEnterTicket.text = context.getString(R.string.main_btn_ticket_enable_time)
                binding.btnEnterTicket.isEnabled = true
            }
        }
    }

    fun setTvTicketState(binding: ActivityMainBinding, ticket: TicketUiModel) {
        val context = binding.root.context
        binding.tvTicketState.text = when (ticket.ticketState) {
            TicketStateUiModel.BEFORE_ENTRY -> context.getString(R.string.main_tv_ticket_before_entry)
            TicketStateUiModel.AFTER_ENTRY -> context.getString(R.string.main_tv_ticket_after_entry)
            TicketStateUiModel.AWAY -> context.getString(R.string.main_tv_ticket_away)
            else -> ""
        }
    }
}
