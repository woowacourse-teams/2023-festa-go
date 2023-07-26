package com.festago.festago.presentation.ui.ticketreserve.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketReserveBottomSheetBinding
import com.festago.festago.presentation.model.ReservationTicketUiModel

class TicketReserveBottomViewHolder(
    private val binding: ItemTicketReserveBottomSheetBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationTicketUiModel) {
        binding.reservationTicket = item
    }

    companion object {
        fun from(parent: ViewGroup): TicketReserveBottomViewHolder {
            val binding = ItemTicketReserveBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketReserveBottomViewHolder(binding)
        }
    }
}
