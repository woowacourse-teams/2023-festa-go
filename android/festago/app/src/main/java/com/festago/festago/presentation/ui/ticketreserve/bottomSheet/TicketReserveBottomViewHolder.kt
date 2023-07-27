package com.festago.festago.presentation.ui.ticketreserve.bottomSheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketReserveBottomSheetBinding

class TicketReserveBottomViewHolder(
    private val binding: ItemTicketReserveBottomSheetBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TicketReserveBottomItem, callback: TicketReserveBottomSheetCallback) {
        binding.reservationTicket = item.ticket
        binding.clLayout.isSelected = item.isSelected
        binding.clLayout.setOnClickListener { callback.invoke(bindingAdapterPosition) }
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
