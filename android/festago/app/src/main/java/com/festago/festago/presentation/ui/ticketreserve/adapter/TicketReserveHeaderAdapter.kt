package com.festago.festago.presentation.ui.ticketreserve.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.festago.festago.presentation.ui.ticketreserve.ReservationFestivalUiState
import com.festago.festago.presentation.ui.ticketreserve.viewHolder.TicketReserveHeaderViewHolder

class TicketReserveHeaderAdapter :
    ListAdapter<ReservationFestivalUiState, TicketReserveHeaderViewHolder>(diffUtil) {

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
        val diffUtil = object : DiffUtil.ItemCallback<ReservationFestivalUiState>() {
            override fun areContentsTheSame(
                oldItem: ReservationFestivalUiState,
                newItem: ReservationFestivalUiState,
            ) = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: ReservationFestivalUiState,
                newItem: ReservationFestivalUiState
            ) =
                oldItem.id == newItem.id
        }
    }
}
