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
        return TicketReserveBottomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TicketReserveBottomViewHolder, position: Int) {
        val callback = TicketReserveBottomSheetCallback {
            currentList.forEach { it.isSelected = false }
            getItem(position).isSelected = true
            notifyDataSetChanged()
        }

        holder.bind(getItem(position), callback)
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
