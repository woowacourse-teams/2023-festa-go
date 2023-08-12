package com.festago.festago.presentation.ui.ticketreserve.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.TicketReserveItemUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel
import com.festago.festago.presentation.ui.ticketreserve.viewHolder.TicketReserveViewHolder

class TicketReserveAdapter(
    private val vm: TicketReserveViewModel,
) : ListAdapter<TicketReserveItemUiModel, TicketReserveViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketReserveViewHolder {
        return TicketReserveViewHolder.of(parent, vm)
    }

    override fun onBindViewHolder(holder: TicketReserveViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TicketReserveItemUiModel>() {
            override fun areContentsTheSame(
                oldItem: TicketReserveItemUiModel,
                newItem: TicketReserveItemUiModel,
            ) = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: TicketReserveItemUiModel,
                newItem: TicketReserveItemUiModel,
            ) = oldItem.id == newItem.id
        }
    }
}
