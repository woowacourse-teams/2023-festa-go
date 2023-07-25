package com.festago.festago.presentation.ui.ticketreservation.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketReservationHeaderBinding
import com.festago.festago.presentation.model.ReservationUiModel

class ReservationHeaderViewHolder(
    private val binding: ItemTicketReservationHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationUiModel) {
        // TODO("Not yet implemented")
    }

    companion object {
        fun from(parent: ViewGroup): ReservationHeaderViewHolder {
            val binding = ItemTicketReservationHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ReservationHeaderViewHolder(binding)
        }
    }
}
