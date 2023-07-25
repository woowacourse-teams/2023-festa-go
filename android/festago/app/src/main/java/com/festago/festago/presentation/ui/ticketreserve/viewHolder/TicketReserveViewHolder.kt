package com.festago.festago.presentation.ui.ticketreserve.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketReserveBinding
import com.festago.festago.presentation.model.ReservationUiModel

class TicketReserveViewHolder(
    private val binding: ItemTicketReserveBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationUiModel) {
    }

    companion object {
        fun from(parent: ViewGroup): TicketReserveViewHolder {
            val binding = ItemTicketReserveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketReserveViewHolder(binding)
        }
    }
}
