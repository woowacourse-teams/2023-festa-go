package com.festago.festago.presentation.ui.tickethistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketHistoryBinding

class TicketHistoryViewHolder(
    val binding: ItemTicketHistoryBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TicketHistoryItemUiState) {
        binding.ticket = item
    }

    companion object {
        fun from(parent: ViewGroup): TicketHistoryViewHolder {
            val binding = ItemTicketHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketHistoryViewHolder(binding)
        }
    }
}
