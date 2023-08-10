package com.festago.festago.presentation.ui.tickethistory

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.TicketUiModel

class TicketHistoryAdapter :
    ListAdapter<TicketUiModel, TicketHistoryViewHolder>(ticketDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketHistoryViewHolder {
        return TicketHistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TicketHistoryViewHolder, position: Int) {
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
