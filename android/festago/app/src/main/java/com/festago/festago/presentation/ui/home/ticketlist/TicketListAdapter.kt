package com.festago.festago.presentation.ui.home.ticketlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.TicketUiModel

class TicketListAdapter(
    private val vm: TicketListViewModel,
) : ListAdapter<TicketUiModel, TicketItemViewHolder>(ticketDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketItemViewHolder {
        return TicketItemViewHolder.of(parent, vm)
    }

    override fun onBindViewHolder(holder: TicketItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val ticketDiffUtil = object : DiffUtil.ItemCallback<TicketUiModel>() {
            override fun areItemsTheSame(oldItem: TicketUiModel, newItem: TicketUiModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TicketUiModel, newItem: TicketUiModel) =
                oldItem == newItem
        }
    }
}
