package com.festago.festago.presentation.ui.ticketreserve.bottomSheet

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class TicketReserveBottomSheetAdapter :
    ListAdapter<TicketReserveBottomItem, TicketReserveBottomViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TicketReserveBottomViewHolder {
        return TicketReserveBottomViewHolder.from(parent) { position ->
            currentList.forEach { it.isSelected = false }
            getItem(position).isSelected = true
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: TicketReserveBottomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TicketReserveBottomItem>() {
            override fun areContentsTheSame(
                oldItem: TicketReserveBottomItem,
                newItem: TicketReserveBottomItem,
            ) = oldItem.ticket == newItem.ticket

            override fun areItemsTheSame(
                oldItem: TicketReserveBottomItem,
                newItem: TicketReserveBottomItem,
            ) = oldItem.ticket.ticketType == newItem.ticket.ticketType
        }
    }
}
