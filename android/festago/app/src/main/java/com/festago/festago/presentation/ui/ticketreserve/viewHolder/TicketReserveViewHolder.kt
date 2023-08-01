package com.festago.festago.presentation.ui.ticketreserve.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.R
import com.festago.festago.databinding.ItemTicketReserveBinding
import com.festago.festago.presentation.model.ReservationStageUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel

class TicketReserveViewHolder(
    private val binding: ItemTicketReserveBinding,
    vm: TicketReserveViewModel,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.vm = vm
    }

    fun bind(item: ReservationStageUiModel) {
        binding.stage = item

        binding.tvTicketCount.text =
            item.reservationTickets.joinToString(binding.root.context.getString(R.string.ticket_reserve_tv_ticket_count_separator)) {
                binding.root.context.getString(
                    R.string.ticket_reserve_tv_ticket_count,
                    it.ticketType,
                    it.remainAmount.toString(),
                    it.totalAmount.toString(),
                )
            }
    }

    companion object {
        fun of(
            parent: ViewGroup,
            vm: TicketReserveViewModel,
        ): TicketReserveViewHolder {
            val binding = ItemTicketReserveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketReserveViewHolder(binding, vm)
        }
    }
}
