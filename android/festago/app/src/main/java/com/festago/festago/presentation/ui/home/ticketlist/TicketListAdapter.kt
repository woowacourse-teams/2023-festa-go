package com.festago.festago.presentation.ui.home.ticketlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.MemberTicketUiModel

class TicketListAdapter(
    private val vm: TicketListViewModel,
) : ListAdapter<MemberTicketUiModel, TicketListItemViewHolder>(ticketDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketListItemViewHolder {
        return TicketListItemViewHolder.of(parent, vm)
    }

    override fun onBindViewHolder(holder: TicketListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val ticketDiffUtil = object : DiffUtil.ItemCallback<MemberTicketUiModel>() {
            override fun areItemsTheSame(
                oldItem: MemberTicketUiModel,
                newItem: MemberTicketUiModel,
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MemberTicketUiModel,
                newItem: MemberTicketUiModel,
            ) = oldItem == newItem
        }
    }
}
