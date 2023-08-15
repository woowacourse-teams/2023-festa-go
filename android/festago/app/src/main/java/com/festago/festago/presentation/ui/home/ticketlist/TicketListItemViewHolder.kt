package com.festago.festago.presentation.ui.home.ticketlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.R
import com.festago.festago.databinding.ItemTicketListBinding
import com.festago.festago.presentation.model.TicketListItemUiState
import java.time.format.DateTimeFormatter

class TicketListItemViewHolder(
    val binding: ItemTicketListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TicketListItemUiState) {
        binding.ticket = item
        setTicketEntryBtn(item)
    }

    private fun setTicketEntryBtn(item: TicketListItemUiState) {
        val btn = binding.btnTicketEntry
        btn.isEnabled = item.canEntry

        setTicketEntryBtnText(isAfterEntryTime = item.canEntry, btn = btn, ticket = item)
    }

    private fun setTicketEntryBtnText(
        isAfterEntryTime: Boolean,
        btn: Button,
        ticket: TicketListItemUiState,
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
        fun from(parent: ViewGroup): TicketListItemViewHolder {
            val binding = ItemTicketListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketListItemViewHolder(binding)
        }
    }
}
