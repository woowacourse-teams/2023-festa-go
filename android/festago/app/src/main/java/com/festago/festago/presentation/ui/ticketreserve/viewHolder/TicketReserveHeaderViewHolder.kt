package com.festago.festago.presentation.ui.ticketreserve.viewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.festago.festago.R
import com.festago.festago.databinding.ItemTicketReserveHeaderBinding
import com.festago.festago.presentation.ui.ticketreserve.ReservationFestivalUiState
import java.time.format.DateTimeFormatter

class TicketReserveHeaderViewHolder(
    private val binding: ItemTicketReserveHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationFestivalUiState) {
        val formatter = DateTimeFormatter.ofPattern(
            binding.root.context.getString(R.string.ticket_reserve_tv_date_range_format)
        )

        binding.tvDateRange.text =
            binding.root.context.getString(R.string.ticket_reserve_tv_date_range)
                .format(item.startDate.format(formatter), item.endDate.format(formatter))
        binding.tvUniversityName.text = item.name

        Glide.with(binding.root)
            .load(item.thumbnail)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivPoster)
    }

    companion object {
        fun from(parent: ViewGroup): TicketReserveHeaderViewHolder {
            val binding = ItemTicketReserveHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
            return TicketReserveHeaderViewHolder(binding)
        }
    }
}
