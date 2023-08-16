package com.festago.festago.presentation.ui.ticketreserve.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.R
import com.festago.festago.databinding.ItemTicketReserveBinding
import com.festago.festago.presentation.ui.ticketreserve.TicketReserveItemUiState
import java.time.format.DateTimeFormatter

class TicketReserveViewHolder(
    private val binding: ItemTicketReserveBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TicketReserveItemUiState) {
        binding.stage = item
        binding.btnReserveTicket.isEnabled = !item.isSigned || item.canReserve

        when {
            !item.isSigned -> {
                binding.btnReserveTicket.text =
                    binding.root.context.getString(R.string.ticket_reserve_tv_signin)
            }

            item.canReserve -> {
                binding.btnReserveTicket.text =
                    binding.root.context.getString(R.string.ticket_reserve_tv_btn_reserve_ticket)
            }

            else -> {
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
