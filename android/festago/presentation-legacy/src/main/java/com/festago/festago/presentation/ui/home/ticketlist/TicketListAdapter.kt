package com.festago.festago.presentation.ui.home.ticketlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class TicketListAdapter :
    ListAdapter<TicketListItemUiState, TicketListItemViewHolder>(ticketDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketListItemViewHolder {
        return TicketListItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TicketListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val ticketDiffUtil = object : DiffUtil.ItemCallback<TicketListItemUiState>() {
            override fun areItemsTheSame(
                oldItem: TicketListItemUiState,
                newItem: TicketListItemUiState,
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TicketListItemUiState,
                newItem: TicketListItemUiState,
            ) = oldItem == newItem
        }
    }
}
