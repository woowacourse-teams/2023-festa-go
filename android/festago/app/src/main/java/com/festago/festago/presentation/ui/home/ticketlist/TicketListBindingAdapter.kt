package com.festago.festago.presentation.ui.home.ticketlist

import android.widget.Button
import androidx.databinding.BindingAdapter
import com.festago.festago.R
import com.festago.festago.presentation.model.TicketUiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("btnTicketEntry")
fun Button.setBtnTicketEntry(ticket: TicketUiModel) {
    val isAfterEntryTime = LocalDateTime.now().isAfter(ticket.entryTime)
    isEnabled = isAfterEntryTime

    if (isAfterEntryTime) {
        text = context.getString(R.string.ticket_list_btn_ticket_entry)
        return
    }
    text = DateTimeFormatter.ofPattern(context.getString(R.string.ticket_list_btn_ticket_disable_time))
        .format(ticket.entryTime)
}
