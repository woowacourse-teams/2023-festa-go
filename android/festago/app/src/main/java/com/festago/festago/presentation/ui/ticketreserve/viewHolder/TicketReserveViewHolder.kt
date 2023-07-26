package com.festago.festago.presentation.ui.ticketreserve.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.festago.festago.R
import com.festago.festago.databinding.ItemTicketReserveBinding
import com.festago.festago.presentation.model.ReservationStageUiModel
import java.time.format.DateTimeFormatter
import java.util.Locale

class TicketReserveViewHolder(
    private val binding: ItemTicketReserveBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationStageUiModel) {
        val openTimeFormatter = DateTimeFormatter.ofPattern(binding.root.context.getString(R.string.ticket_reserve_tv_open_time))
        binding.tvOpenTime.text = item.ticketOpenTime.format(openTimeFormatter)

        val dateFormatter = DateTimeFormatter.ofPattern(binding.root.context.getString(R.string.ticket_reserve_tv_start_time), Locale.KOREA)
        binding.tvDate.text = item.startTime.format(dateFormatter)

        binding.tvLineUp.text = item.lineUp

        binding.tvTicketCount.text = item.reservationTickets
            .joinToString(", ") { "${it.ticketType}(${it.remainAmount}/${it.totalAmount})" }
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
