package com.festago.festago.presentation.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.festago.festago.R
import com.festago.festago.presentation.model.TicketStateUiModel

@BindingAdapter("ticketState")
fun setTicketState(textView: TextView, ticketStateUiModel: TicketStateUiModel?) {
    when (ticketStateUiModel) {
        TicketStateUiModel.BEFORE_ENTRY -> {
            textView.setText(R.string.ticket_entry_tv_before_entry)
            textView.isEnabled = true
        }
        TicketStateUiModel.AFTER_ENTRY -> {
            textView.setText(R.string.ticket_entry_tv_after_entry)
            textView.isEnabled = false
        }
        TicketStateUiModel.AWAY -> {
            textView.setText(R.string.ticket_entry_tv_away)
            textView.isEnabled = true
        }
        null -> ""
    }
}
