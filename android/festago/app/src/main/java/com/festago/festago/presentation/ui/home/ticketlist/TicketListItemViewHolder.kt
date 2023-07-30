package com.festago.festago.presentation.ui.home.ticketlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.R
import com.festago.festago.databinding.ItemTicketListBinding
import com.festago.festago.presentation.model.TicketUiModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TicketListItemViewHolder(
    val binding: ItemTicketListBinding,
    vm: TicketListViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.vm = vm
    }

    fun bind(item: TicketUiModel) {
        binding.ticket = item
        setTicketEntryBtn(item)
    }

    private fun setTicketEntryBtn(item: TicketUiModel) {
        val btn = binding.btnTicketEntry
        val isAfterEntryTime = LocalDateTime.now().isAfter(item.entryTime)
        btn.isEnabled = isAfterEntryTime

        setTicketEntryBtnText(isAfterEntryTime = isAfterEntryTime, btn = btn, ticket = item)
    }

    private fun setTicketEntryBtnText(
        isAfterEntryTime: Boolean,
        btn: Button,
        ticket: TicketUiModel,
    ) {
        if (isAfterEntryTime) {
            btn.text = btn.context.getString(R.string.ticket_list_btn_ticket_entry)
            return
        }
        btn.text = DateTimeFormatter.ofPattern(
            btn.context.getString(
                R.string.ticket_list_btn_ticket_disable_time,
            ),
        ).format(ticket.entryTime)
    }

    companion object {
        fun of(parent: ViewGroup, vm: TicketListViewModel): TicketListItemViewHolder {
            val binding = ItemTicketListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketListItemViewHolder(binding, vm)
        }
    }
}
