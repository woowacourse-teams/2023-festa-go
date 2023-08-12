package com.festago.festago.presentation.ui.ticketreserve.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.R
import com.festago.festago.databinding.ItemTicketReserveBinding
import com.festago.festago.presentation.model.TicketReserveItemUiModel
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveViewModel
import java.time.format.DateTimeFormatter

class TicketReserveViewHolder(
    private val binding: ItemTicketReserveBinding,
    vm: TicketReserveViewModel,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.vm = vm
    }

    fun bind(item: TicketReserveItemUiModel) {
        binding.stage = item

        when {
            !item.isSigned -> {
                binding.btnReserveTicket.isEnabled = true
                binding.btnReserveTicket.text =
                    binding.root.context.getString(R.string.ticket_reserve_tv_signin)
            }

            item.canReserve -> {
                binding.btnReserveTicket.isEnabled = true
                binding.btnReserveTicket.text =
                    binding.root.context.getString(R.string.ticket_reserve_tv_btn_reserve_ticket)
            }

            else -> {
                binding.btnReserveTicket.isEnabled = false
                val pattern = DateTimeFormatter.ofPattern(
                    binding.root.context.getString(R.string.ticket_reserve_tv_btn_reserve_ticket_not_open),
                )
                binding.btnReserveTicket.text = item.ticketOpenTime.format(pattern)
            }
        }

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
