package com.festago.festago.presentation.ui.ticketreserve.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.model.ReservationUiModel
import com.festago.festago.presentation.ui.ticketreserve.viewHolder.TicketReserveHeaderViewHolder

class TicketReserveHeaderAdapter :
    ListAdapter<ReservationUiModel, TicketReserveHeaderViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TicketReserveHeaderViewHolder {
        return TicketReserveHeaderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TicketReserveHeaderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ReservationUiModel>() {
            override fun areContentsTheSame(
                oldItem: ReservationUiModel,
                newItem: ReservationUiModel,
            ) = oldItem == newItem

            override fun areItemsTheSame(oldItem: ReservationUiModel, newItem: ReservationUiModel) =
                oldItem.id == newItem.id
        }
    }
}
