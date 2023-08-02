package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.databinding.ItemTicketReserveBottomSheetBinding

class TicketReserveBottomViewHolder(
    private val binding: ItemTicketReserveBottomSheetBinding,
    private val callback: TicketReserveBottomSheetCallback,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.clLayout.setOnClickListener { callback(bindingAdapterPosition) }
    }

    fun bind(item: TicketReserveBottomItem) {
        binding.item = item
        binding.clLayout.isSelected = item.isSelected
    }

    companion object {
        fun of(
            parent: ViewGroup,
            callback: TicketReserveBottomSheetCallback,
        ): TicketReserveBottomViewHolder {
            val binding = ItemTicketReserveBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketReserveBottomViewHolder(binding, callback)
        }
    }
}
