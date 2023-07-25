package com.festago.festago.presentation.ui.ticketreserve.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketReserveHeaderBinding
import com.festago.festago.presentation.model.ReservationUiModel

class TicketReserveHeaderViewHolder(
    private val binding: ItemTicketReserveHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationUiModel) {
        // TODO("Not yet implemented")
    }

    companion object {
        fun from(parent: ViewGroup): TicketReserveHeaderViewHolder {
            val binding = ItemTicketReserveHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketReserveHeaderViewHolder(binding)
        }
    }
}
