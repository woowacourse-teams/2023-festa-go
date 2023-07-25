package com.festago.festago.presentation.ui.ticketreservation.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketReservationBinding
import com.festago.festago.presentation.model.ReservationUiModel

class ReservationViewHolder(
    private val binding: ItemTicketReservationBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationUiModel) {
    }

    companion object {
        fun from(parent: ViewGroup): ReservationViewHolder {
            val binding = ItemTicketReservationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return ReservationViewHolder(binding)
        }
    }
}
