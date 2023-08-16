package com.festago.festago.presentation.ui.ticketreserve.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.TicketReserveItemUiState
import com.festago.festago.presentation.ui.ticketreserve.viewHolder.TicketReserveViewHolder

class TicketReserveAdapter :
    ListAdapter<TicketReserveItemUiState, TicketReserveViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketReserveViewHolder {
        return TicketReserveViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TicketReserveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<TicketReserveItemUiState>() {
            override fun areContentsTheSame(
                oldItem: TicketReserveItemUiState,
                newItem: TicketReserveItemUiState,
            ) = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: TicketReserveItemUiState,
                newItem: TicketReserveItemUiState,
            ) = oldItem.id == newItem.id
        }
    }
}
