package com.festago.festago.presentation.ui.tickethistory

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class TicketHistoryAdapter : ListAdapter<TicketHistoryItemUiState, TicketHistoryViewHolder>(ticketDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketHistoryViewHolder {
        return TicketHistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TicketHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val ticketDiffUtil = object : DiffUtil.ItemCallback<TicketHistoryItemUiState>() {
            override fun areItemsTheSame(oldItem: TicketHistoryItemUiState, newItem: TicketHistoryItemUiState) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TicketHistoryItemUiState, newItem: TicketHistoryItemUiState) =
                oldItem == newItem
        }
    }
}
