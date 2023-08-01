package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class TicketReserveBottomSheetAdapter :
    ListAdapter<TicketReserveBottomItem, TicketReserveBottomViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TicketReserveBottomViewHolder {
        return TicketReserveBottomViewHolder.of(parent) { position ->
            val newList = currentList.mapIndexed { index, item ->
                item.copy(isSelected = (index == position))
            }
            submitList(newList)
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
            ) = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: TicketReserveBottomItem,
                newItem: TicketReserveBottomItem,
            ) = oldItem.ticket.ticketType == newItem.ticket.ticketType
        }
    }
}
