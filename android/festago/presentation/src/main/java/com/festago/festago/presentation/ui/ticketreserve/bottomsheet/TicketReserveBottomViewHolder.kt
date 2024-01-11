package com.festago.festago.presentation.ui.ticketreserve.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.ItemTicketReserveBottomSheetBinding

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
        binding.tvTicketType.text = item.ticket.ticketType.getString()
    }

    private fun BottomSheetTicketTypeArg.getString(): String {
        val resId: Int = when (this) {
            BottomSheetTicketTypeArg.STUDENT -> R.string.all_ticket_type_student
            BottomSheetTicketTypeArg.VISITOR -> R.string.all_ticket_type_visitor
            BottomSheetTicketTypeArg.OTHER -> R.string.all_ticket_type_other
        }
        return binding.root.context.getString(resId)
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
